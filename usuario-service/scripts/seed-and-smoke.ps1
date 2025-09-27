<#
  Seeds test users (ADMIN, MEDICO, ENFERMEIRO, PACIENTE), logs in as ADMIN to get a JWT,
  lists users to capture their IDs, and runs smoke tests against /autorizacoes/validar-acesso.

  Requirements: Windows PowerShell 5.1+, curl.exe available on PATH (bundled with Windows 10+).
#>

param(
  [string]$BaseUrl = 'http://localhost:3000'
)

function Invoke-CurlJson {
  param(
    [Parameter(Mandatory=$true)][string]$Method,
    [Parameter(Mandatory=$true)][string]$Url,
    [string]$BodyJson,
    [string]$Token
  )

  $args = @('-s')
  if ($Method -ieq 'POST' -or $Method -ieq 'PUT' -or $Method -ieq 'PATCH' -or $Method -ieq 'DELETE') {
    $args += @('-X', $Method)
  } else {
    $args += @('-X', $Method)
  }
  $args += @('-H', 'Content-Type: application/json')
  if ($Token) { $args += @('-H', "Authorization: Bearer $Token") }
  if ($BodyJson) {
    $tmp = New-TemporaryFile
    Set-Content -Path $tmp -Value $BodyJson -Encoding ascii
    $args += @('--data-binary', '@' + $tmp)
  }
  $args += @('-w', 'HTTP_STATUS:%{http_code}')
  $args += @($Url)

  $raw = & curl.exe @args
  if ($tmp) { Remove-Item $tmp -Force -ErrorAction SilentlyContinue }

  # Split body and status
  $m = [regex]::Match($raw, 'HTTP_STATUS:(\d{3})$')
  $status = if ($m.Success) { [int]$m.Groups[1].Value } else { -1 }
  $body = if ($m.Success) { $raw.Substring(0, $m.Index) } else { $raw }
  return [pscustomobject]@{ StatusCode = $status; Body = $body }
}

function Write-Header($text) {
  Write-Host "`n=== $text ===" -ForegroundColor Cyan
}

Write-Header "Health check"
$health = Invoke-CurlJson -Method GET -Url "$BaseUrl/actuator/health" -BodyJson $null -Token $null
Write-Host ("Status: {0} Body: {1}" -f $health.StatusCode, $health.Body)

Write-Header "Seeding users"
$users = @(
  @{ label = 'ADMIN';      json = '{"nome":"Admin Root","cpf":"00000000001","dataNascimento":"1980-01-01","email":"admin@example.com","senha":"Admin@1234","login":"admin","perfilId":0,"telefone":"11999990001"}' },
  @{ label = 'MEDICO';     json = '{"nome":"Dr House","cpf":"00000000002","dataNascimento":"1975-05-05","email":"medico@example.com","senha":"Medico@1234","login":"medico","perfilId":1,"telefone":"11999990002"}' },
  @{ label = 'PACIENTE';   json = '{"nome":"John Doe","cpf":"00000000004","dataNascimento":"1990-09-09","email":"paciente@example.com","senha":"Paciente@1234","login":"paciente","perfilId":2,"telefone":"11999990004"}' },
  @{ label = 'ENFERMEIRO'; json = '{"nome":"Nurse Joy","cpf":"00000000003","dataNascimento":"1985-08-08","email":"enfermeiro@example.com","senha":"Enf@1234","login":"enfermeiro","perfilId":3,"telefone":"11999990003"}' }
)

foreach ($u in $users) {
  $resp = Invoke-CurlJson -Method POST -Url "$BaseUrl/usuarios" -BodyJson $u.json -Token $null
  Write-Host ("{0}: HTTP {1}" -f $u.label, $resp.StatusCode)
  if ($resp.StatusCode -eq 400 -or $resp.StatusCode -eq 409) {
    Write-Host ("  Body: {0}" -f $resp.Body)
  }
}

Write-Header "Login as ADMIN"
$loginBody = '{"login":"admin","senha":"Admin@1234","perfil":"ADMIN"}'
$login = Invoke-CurlJson -Method POST -Url "$BaseUrl/login" -BodyJson $loginBody -Token $null
Write-Host ("Login HTTP: {0}" -f $login.StatusCode)
if ($login.StatusCode -ne 200) {
  Write-Error "Login failed. Body: $($login.Body)"; exit 1
}
$token = try { ($login.Body | ConvertFrom-Json).token } catch { $null }
if (-not $token) { Write-Error "Token not found in response: $($login.Body)"; exit 1 }
Write-Host "Token acquired."

Write-Header "List users (capture IDs)"
$list = Invoke-CurlJson -Method GET -Url "$BaseUrl/usuarios" -BodyJson $null -Token $token
Write-Host ("List HTTP: {0}" -f $list.StatusCode)
if ($list.StatusCode -eq 204) { Write-Error "No users found (204)."; exit 1 }
if ($list.StatusCode -ne 200) { Write-Error ("List failed: {0}" -f $list.Body); exit 1 }

$usersArr = $null
try { $usersArr = $list.Body | ConvertFrom-Json } catch { Write-Error "Failed to parse list JSON."; exit 1 }

function Find-UserId($login) {
  ($usersArr | Where-Object { $_.login -eq $login } | Select-Object -First 1).id
}

$ADMIN_ID = Find-UserId 'admin'
$MEDICO_ID = Find-UserId 'medico'
$ENFERMEIRO_ID = Find-UserId 'enfermeiro'
$PACIENTE_ID = Find-UserId 'paciente'

Write-Host "IDs -> ADMIN=$ADMIN_ID; MEDICO=$MEDICO_ID; ENFERMEIRO=$ENFERMEIRO_ID; PACIENTE=$PACIENTE_ID"

if (-not $MEDICO_ID -or -not $ENFERMEIRO_ID -or -not $PACIENTE_ID) {
  Write-Warning "Some user IDs are missing. Proceeding anyway."
}

Write-Header "Authorization smoke tests"
function Test-Auth {
  param(
    [string]$Label,
    [int64]$UsuarioId,
    [string]$UrlPath,
    [string]$ExpectCodigo
  )
  $body = (@{ usuarioId = $UsuarioId; url = $UrlPath } | ConvertTo-Json -Compress)
  $r = Invoke-CurlJson -Method POST -Url "$BaseUrl/autorizacoes/validar-acesso" -BodyJson $body -Token $token
  $codigo = $null; $autorizado = $null
  try {
    $obj = $r.Body | ConvertFrom-Json
    $codigo = $obj.codigo; $autorizado = $obj.autorizado
  } catch {}
  $ok = if ($ExpectCodigo) { $codigo -eq $ExpectCodigo } else { $true }
  Write-Host ("[{0}] HTTP {1} -> codigo={2} autorizado={3} url={4}" -f $Label, $r.StatusCode, $codigo, $autorizado, $UrlPath) -ForegroundColor (if ($ok) { 'Green' } else { 'Yellow' })
  if (-not $ok) { Write-Host "  Body: $($r.Body)" }
}

Test-Auth -Label 'MEDICO pode editar consultas' -UsuarioId $MEDICO_ID -UrlPath '/api/agendamento/edicao' -ExpectCodigo 'PERMITIDO'
Test-Auth -Label 'ENFERMEIRO pode criar consultas' -UsuarioId $ENFERMEIRO_ID -UrlPath '/api/agendamento/criacao' -ExpectCodigo 'PERMITIDO'
Test-Auth -Label 'MEDICO pode visualizar histórico' -UsuarioId $MEDICO_ID -UrlPath '/api/historico/visualizar' -ExpectCodigo 'PERMITIDO'
Test-Auth -Label 'PACIENTE pode visualizar histórico' -UsuarioId $PACIENTE_ID -UrlPath '/api/historico/visualizar' -ExpectCodigo 'PERMITIDO'
Test-Auth -Label 'URL desconhecida nega' -UsuarioId $ADMIN_ID -UrlPath '/api/nao-existe/acao' -ExpectCodigo 'URL_DESCONHECIDA'

Write-Host "`nDone."
