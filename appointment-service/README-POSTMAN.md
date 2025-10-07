# Coleção Postman - Appointment Service

Este arquivo contém uma coleção completa do Postman para testar o serviço `appointment-service` do sistema hospitalar.

## 📋 Configuração

### Base URL
- **Desenvolvimento**: `http://localhost:3005`
- **Porta padrão**: 3005 (conforme `application.yml`)

### Usuários de Teste

A coleção inclui requisições de login para os seguintes usuários:

| ID  | Nome               | CPF         | Login      | Senha    | Perfil     |
|-----|-------------------|-------------|------------|----------|------------|
| 101 | Medico Exemplo    | 11111111111 | medico     | senha123 | MEDICO     |
| 102 | Paciente Exemplo  | 22222222222 | paciente   | senha123 | PACIENTE   |
| 103 | Enfermeiro        | 33333333333 | enfermeiro | senha123 | ENFERMEIRO |

## 🚀 Como Usar

### 1. Subir o projeto

A partir da raiz do projeto:

```docker-compose up --build```

### 2. Importar no Postman
1. Abra o Postman
2. Clique em "Import"
3. Selecione o arquivo `Appointment Service API.postman_collection.json`
4. A coleção será importada com todas as requisições configuradas

### 3. Variáveis de Ambiente
A coleção utiliza as seguintes variáveis:
- `baseUrl`: URL base da API (padrão: `http://localhost:3005`)
- `baseUrlGraphQL`: URL base da API do GraphQL (padrão: `http://localhost:3003`)
- `authToken`: Token JWT (preenchido automaticamente após login)

### 4. Fluxo de Teste Recomendado

#### 1️⃣ Hospital
Crie um hospital primeiro.
- **Criar Hospital**: Aberto para qualquer perfil

#### 2️⃣ Autenticação
Execute uma das requisições de login primeiro:
- **Login - Médico**: Para criar/editar agendamentos e consultar históricos
- **Login - Paciente**: Para consultar próprio histórico
- **Login - Enfermeiro**: Para criar agendamentos e consultar históricos

> ⚠️ **Importante**: O token JWT é automaticamente salvo após o login bem-sucedido.

#### 3️⃣ Agendamentos
- **Criar Agendamento**: Requer perfil MEDICO ou ENFERMEIRO
- **Editar Agendamento**: Requer perfil MEDICO
- Exemplos incluem agendamentos entre o médico (ID: 101) e paciente (ID: 102)

#### 4️⃣ Histórico
Acesso via GraphQL.
- Guia dedicado de testes [aqui](../historico-service/GUIA_TESTES_GRAPHQL.md)

## 🔐 Autorização e Permissões

### Endpoints e Permissões Requeridas:

| Endpoint | Método | Perfis Autorizados | Descrição |
|----------|--------|--------------------|-----------|
| `/api/auth/login` | POST | Público | Autenticação |
| `/api/agendamento/criacao` | POST | MEDICO, ENFERMEIRO | Criar agendamento |
| `/api/agendamento/{id}` | PUT | MEDICO | Editar agendamento |

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
  "hospitalId": 1,
  "especialidadeId": 1,
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
    "pacienteId": 102
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