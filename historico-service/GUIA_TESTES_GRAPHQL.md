# Guia de Testes - GraphQL Histórico Médico

## 1. Configuração Inicial

### Pré-requisitos:
- Serviço histórico-service rodando na porta 3003
- Banco de dados PostgreSQL configurado
- Postman instalado

### URLs importantes:
- **GraphQL Endpoint**: `http://localhost:3003/graphql`
- **GraphiQL Interface**: `http://localhost:3003/graphiql`

## 2. Testando via GraphiQL (Recomendado para desenvolvimento)

1. Abra o navegador e acesse: `http://localhost:3003/graphiql`
2. Você verá uma interface interativa para testar queries GraphQL
3. Execute as queries de exemplo abaixo:

### Query 1: Todos os atendimentos de um paciente
```graphql
query {
  todosAtendimentosPaciente(pacienteId: "1") {
    id
    nomePaciente
    nomeMedico
    nomeHospital
    especializacao
    statusAgendamento
    dataHoraAgendamento
  }
}
```

### Query 2: Apenas atendimentos futuros
```graphql
query {
  atendimentosFuturosPaciente(pacienteId: "1") {
    id
    nomePaciente
    nomeMedico
    dataHoraAgendamento
    statusAgendamento
  }
}
```

### Query 3: Atendimentos por status
```graphql
query {
  atendimentosPorStatus(pacienteId: "1", status: CRIADA) {
    id
    nomePaciente
    nomeMedico
    statusAgendamento
    dataHoraAgendamento
  }
}
```

### Query 4: Atendimentos em um período específico
```graphql
query {
  atendimentosPorPeriodo(
    pacienteId: "1", 
    dataInicio: "2024-01-01T00:00:00", 
    dataFim: "2024-12-31T23:59:59"
  ) {
    id
    nomePaciente
    nomeMedico
    nomeHospital
    dataHoraAgendamento
  }
}
```

## 3. Testando via Postman

### Configuração no Postman:

1. **Importe a Collection**:
   - Abra o Postman
   - Clique em "Import"
   - Selecione o arquivo: `historico-service/postman/GraphQL-Historico-Medico.postman_collection.json`

2. **Importe o Environment**:
   - Vá em "Environments"
   - Clique em "Import"
   - Selecione: `historico-service/postman/GraphQL-Environment.postman_environment.json`

3. **Configuração da Requisição**:
   - **Method**: POST
   - **URL**: `http://localhost:3003/graphql`
   - **Headers**: 
     ```
     Content-Type: application/json
     ```
   - **Body** (raw JSON):
     ```json
     {
       "query": "query { todosAtendimentosPaciente(pacienteId: \"1\") { id nomePaciente nomeMedico statusAgendamento } }"
     }
     ```

### Exemplos de Requisições Postman:

#### 1. Consulta Simples:
```json
{
  "query": "query { todosAtendimentosPaciente(pacienteId: \"1\") { id nomePaciente nomeMedico nomeHospital statusAgendamento dataHoraAgendamento } }"
}
```

#### 2. Consulta com Variáveis:
```json
{
  "query": "query BuscarHistorico($pacienteId: ID!, $status: StatusAgendamento!) { atendimentosPorStatus(pacienteId: $pacienteId, status: $status) { id nomePaciente nomeMedico statusAgendamento dataHoraAgendamento } }",
  "variables": {
    "pacienteId": "1",
    "status": "CRIADA"
  }
}
```

#### 3. Múltiplas Consultas em uma Requisição:
```json
{
  "query": "query { todos: todosAtendimentosPaciente(pacienteId: \"1\") { id nomePaciente statusAgendamento } futuros: atendimentosFuturosPaciente(pacienteId: \"1\") { id dataHoraAgendamento } }"
}
```

## 4. Testando via cURL

### Exemplo básico:
```bash
curl -X POST \
  http://localhost:3003/graphql \
  -H 'Content-Type: application/json' \
  -d '{
    "query": "query { todosAtendimentosPaciente(pacienteId: \"1\") { id nomePaciente nomeMedico statusAgendamento } }"
  }'
```

### Com variáveis:
```bash
curl -X POST \
  http://localhost:3003/graphql \
  -H 'Content-Type: application/json' \
  -d '{
    "query": "query($pacienteId: ID!) { todosAtendimentosPaciente(pacienteId: $pacienteId) { id nomePaciente } }",
    "variables": { "pacienteId": "1" }
  }'
```

## 5. Tipos de Resposta Esperados

### Sucesso:
```json
{
  "data": {
    "todosAtendimentosPaciente": [
      {
        "id": "1",
        "nomePaciente": "João Silva",
        "nomeMedico": "Dr. Carlos Santos",
        "nomeHospital": "Hospital São Paulo",
        "statusAgendamento": "CRIADA",
        "dataHoraAgendamento": "2024-12-15T14:30:00"
      }
    ]
  }
}
```

### Erro de Validação:
```json
{
  "errors": [
    {
      "message": "Validation error of type WrongType: argument 'pacienteId' with value 'StringValue{value='abc'}' is not a valid 'ID'",
      "locations": [{"line": 1, "column": 39}]
    }
  ]
}
```

## 6. Testes Unitários

Execute os testes unitários:
```bash
cd historico-service
./mvnw test
```

## 7. Cenários de Teste Recomendados

1. **Teste com dados válidos**: IDs de pacientes existentes
2. **Teste com dados inválidos**: IDs inexistentes, formatos incorretos
3. **Teste de performance**: Consultas com muitos registros
4. **Teste de filtros**: Diferentes status, períodos, etc.
5. **Teste de campos opcionais**: Requisitar apenas campos específicos

## 8. Monitoramento e Debug

- Verifique os logs do serviço para debug
- Use o GraphiQL para explorar o schema
- Monitore as queries no banco de dados
- Verifique métricas de performance

## 9. Exemplos de Queries Avançadas

### Busca por múltiplos critérios:
```graphql
query HistoricoCompleto($pacienteId: ID!) {
  todos: todosAtendimentosPaciente(pacienteId: $pacienteId) {
    id
    statusAgendamento
    dataHoraAgendamento
  }
  
  futuros: atendimentosFuturosPaciente(pacienteId: $pacienteId) {
    id
    nomeMedico
    especializacao
  }
  
  cancelados: atendimentosPorStatus(pacienteId: $pacienteId, status: CANCELADA) {
    id
    dataHoraAgendamento
  }
}
```

### Introspection para descobrir schema:
```graphql
query {
  __schema {
    queryType {
      fields {
        name
        type {
          name
        }
      }
    }
  }
}
```
---
> Referência cruzada: documentação global do projeto no README raiz.