# Coleção Postman - Appointment Service

Este arquivo contém uma coleção completa do Postman para testar o serviço `appointment-service` do sistema hospitalar.

## 📋 Configuração

### Base URL
- **Desenvolvimento**: `http://localhost:3002`
- **Porta padrão**: 3002 (conforme `application.yml`)

### Usuários de Teste

A coleção inclui requisições de login para os seguintes usuários:

| ID  | Nome               | CPF         | Login      | Senha    | Perfil     |
|-----|-------------------|-------------|------------|----------|------------|
| 100 | Administrador     | 00000000000 | admin      | senha123 | ADMIN      |
| 101 | Medico Exemplo    | 11111111111 | medico     | senha123 | MEDICO     |
| 102 | Paciente Exemplo  | 22222222222 | paciente   | senha123 | PACIENTE   |
| 103 | Enfermeiro        | 33333333333 | enfermeiro | senha123 | ENFERMEIRO |

## 🚀 Como Usar

### 1. Importar no Postman
1. Abra o Postman
2. Clique em "Import"
3. Selecione o arquivo `postman-collection-appointment-service.json`
4. A coleção será importada com todas as requisições configuradas

### 2. Variáveis de Ambiente
A coleção utiliza as seguintes variáveis:
- `baseUrl`: URL base da API (padrão: `http://localhost:3002`)
- `authToken`: Token JWT (preenchido automaticamente após login)

### 3. Fluxo de Teste Recomendado

#### 1️⃣ Autenticação
Execute uma das requisições de login primeiro:
- **Login - Admin**: Para operações administrativas
- **Login - Médico**: Para criar/editar agendamentos e consultar históricos
- **Login - Paciente**: Para consultar próprio histórico
- **Login - Enfermeiro**: Para criar agendamentos e consultar históricos

> ⚠️ **Importante**: O token JWT é automaticamente salvo após o login bem-sucedido.

#### 2️⃣ Agendamentos
- **Criar Agendamento**: Requer perfil MEDICO ou ENFERMEIRO
- **Editar Agendamento**: Requer perfil MEDICO
- Exemplos incluem agendamentos entre o médico (ID: 101) e paciente (ID: 102)

#### 3️⃣ Histórico Médico
- **Histórico do Paciente**: Todos os atendimentos (passados e futuros)
- **Atendimentos Futuros do Paciente**: Apenas agendamentos futuros
- **Atendimentos por Médico**: Histórico de um médico específico
- **Atendimentos Futuros por Médico**: Agenda futura do médico

## 🔐 Autorização e Permissões

### Endpoints e Permissões Requeridas:

| Endpoint | Método | Perfis Autorizados | Descrição |
|----------|--------|--------------------|-----------|
| `/api/auth/login` | POST | Público | Autenticação |
| `/api/agendamento/criacao` | POST | MEDICO, ENFERMEIRO | Criar agendamento |
| `/api/agendamento/{id}` | PUT | MEDICO | Editar agendamento |
| `/api/historico/paciente/{id}` | GET | MEDICO, PACIENTE, ENFERMEIRO | Histórico do paciente |
| `/api/historico/paciente/{id}/futuros` | GET | MEDICO, PACIENTE, ENFERMEIRO | Agendamentos futuros do paciente |
| `/api/historico/medico/{id}` | GET | MEDICO, ENFERMEIRO | Histórico do médico |
| `/api/historico/medico/{id}/futuros` | GET | MEDICO, ENFERMEIRO | Agendamentos futuros do médico |

## 📝 Estrutura das Requisições

### Login Request
```json
{
  "login": "string",
  "senha": "string",
  "perfil": "string"
}
```

### Agendamento Request
```json
{
  "pacienteId": 102,
  "medicoId": 101,
  "dataHora": "2025-10-15T10:30:00"
}
```

### Exemplo de Response - Histórico Médico
```json
[
  {
    "id": 1,
    "dataHora": "2025-10-15T10:30:00",
    "status": "AGENDADO",
    "medicoId": 101,
    "pacienteId": 102,
    "descricao": "Consulta de rotina"
  }
]
```

## 🛠️ Troubleshooting

### Problemas Comuns:

1. **Token Expirado**: Execute o login novamente
2. **Acesso Negado**: Verifique se o perfil do usuário tem permissão para o endpoint
3. **Serviço Indisponível**: Verifique se o serviço está rodando na porta 3002
4. **Dados Não Encontrados**: Verifique se os IDs utilizados existem no banco

### Status Codes Esperados:
- `200`: Sucesso
- `401`: Não autorizado (token inválido/expirado)
- `403`: Acesso negado (permissões insuficientes)
- `404`: Recurso não encontrado
- `500`: Erro interno do servidor

## 📞 Suporte

Para dúvidas ou problemas com a API, consulte:
- Logs da aplicação: `appointment-*.log`
- Documentação do código-fonte
- Configurações em: `src/main/resources/application.yml`