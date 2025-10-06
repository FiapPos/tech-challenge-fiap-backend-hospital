# 📬 Postman Collection - Usuario Service API

Esta pasta contém os arquivos necessários para importar e testar os endpoints do Usuario Service no Postman.

## 📁 Arquivos incluídos

- `Usuario-Service-API.fixed.postman_collection.json` — Coleção completa com todos os endpoints (arquivo presente nesta pasta)
- `Usuario-Service.postman_environment.json` — Environment com variáveis pré-configuradas
- `README.md` — Este arquivo com instruções

## 🚀 Como importar no Postman

### 1) Importar a coleção
1. Abra o Postman
2. Clique em “Import” (canto superior esquerdo)
3. Selecione o arquivo `Usuario-Service-API.fixed.postman_collection.json`
4. Clique em “Import”

### 2) Importar o Environment
1. No Postman, clique em “Environments” (canto superior direito)
2. Clique em “Import”
3. Selecione o arquivo `Usuario-Service.postman_environment.json`
4. Clique em “Import”
5. Selecione o environment “Hospital Usuario Service - Environment” no dropdown

## 🔧 Variáveis do Environment

O environment já vem pré-configurado com as variáveis abaixo. Ajuste conforme seu modo de execução:

| Variável | Valor padrão | Descrição |
|---------|---------------|-----------|
| `base_url` | `http://localhost:3001` | URL base quando rodando via Docker Compose (porta externa 3001 → serviço interno 3000) |
| `usuario_id` | `1` | ID do usuário para testes |
| `medico_id` | `1` | ID do médico para testes |
| `especialidade_id` | `1` | ID da especialidade para testes |
| `endereco_id` | `1` | ID do endereço para testes |

Observação:
- Se executar o serviço localmente via Maven (sem Docker), a porta padrão é `http://localhost:3000`. Nesse caso, altere a variável `base_url` para `http://localhost:3000`.

## 📋 Endpoints organizados por categoria

### 👥 Usuários
- Criar Usuário — `POST /api/usuarios`
- Listar Usuários — `GET /api/usuarios`
- Listar Usuários por Especialidade — `GET /api/usuarios/por-especialidade/{especialidadeId}`
- Atualizar Usuário — `PUT /api/usuarios/{id}`
- Desativar Usuário — `DELETE /api/usuarios/{id}`

### 🏥 Especialidades
- Criar Especialidade — `POST /api/especialidades`
- Listar Especialidades — `GET /api/especialidades`
- Atualizar Especialidade — `PATCH /api/especialidades/{id}`
- Inativar Especialidade — `PATCH /api/especialidades/{id}/inativar`

### 👨‍⚕️ Especialidades do Médico
- Associar Especialidade — `POST /medicos/{medicoId}/especialidades/{especialidadeId}`
- Listar Especialidades do Médico — `GET /medicos/{medicoId}/especialidades`
- Desassociar Especialidade — `DELETE /medicos/{medicoId}/especialidades/{especialidadeId}`

### 🏠 Endereços
- Criar Endereço — `POST /enderecos`
- Atualizar Endereço — `PUT /enderecos/{id}`
- Deletar Endereço — `DELETE /enderecos` (com body JSON; ver exemplo abaixo)
- Listar Endereços do Usuário — `GET /enderecos/usuario/{id}`

## 🔑 Fluxo de teste recomendado

1) Autenticação:
- Não é necessário token neste momento (endpoints públicos). Pule esta etapa.

2) Criar Usuário:
- Requisição: `POST /api/usuarios`
- Body (JSON):
```json
{
  "nome": "João Silva",
  "cpf": "12345678909",
  "dataNascimento": "1990-01-01",
  "telefone": "11999999999",
  "email": "joao@email.com",
  "login": "joaosilva",
  "senha": "123456",
  "perfilId": 1
}
```

3) Criar Especialidade:
- Requisição: `POST /api/especialidades`
- Body (JSON):
```json
{
  "nome": "Cardiologia",
  "descricao": "Especialidade médica focada no sistema cardiovascular"
}
```

4) Deletar Endereço:
- Requisição: `DELETE /api/enderecos`
- Body (JSON):
```json
{
  "usuarioId": 1,
  "enderecoId": 1
}
```

5) Teste os demais endpoints conforme necessário.

## ⚠️ Pré-requisitos

1) Banco de dados e Kafka via Docker Compose:
```powershell
docker-compose up -d
```

2) Executar o Usuario Service (escolha uma opção):
- Via Docker Compose (recomendado): a porta externa será `http://localhost:3001`
- Localmente via Maven (dev):
```powershell
cd usuario-service
.\mvnw.cmd spring-boot:run
```
A porta local padrão é `http://localhost:3000`.

3) Postman instalado (desktop ou web).

## 🎯 Dicas para testes

- Token JWT: não é necessário enquanto a segurança estiver desativada.
- IDs dinâmicos: ajuste as variáveis (`usuario_id`, `medico_id`, etc.) conforme os dados criados.
- Autorização: não está habilitada no momento (permitAll). Se ativar no futuro, adicione manualmente o header `Authorization: Bearer <token>` conforme sua estratégia.
- Headers: a coleção já inclui `Content-Type` onde necessário; não há header `Authorization` por padrão.

## 📝 Observações

- A coleção traz exemplos de bodies prontos para facilitar.
- As variáveis ajudam a alternar ambientes sem editar cada requisição.
- Algumas rotas podem retornar `204 No Content` quando não houver dados.

---

✅ Collection pronta para uso. Importe e comece a testar! 🚀
