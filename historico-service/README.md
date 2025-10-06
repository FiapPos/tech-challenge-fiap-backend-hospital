# Histórico Service

Serviço responsável por armazenar e disponibilizar o histórico completo de consultas médicas dos pacientes através de uma API GraphQL.

## 🔌 Portas

| Contexto | URL Base | Porta Interna (Spring) | Observação |
|----------|----------|------------------------|------------|
| Docker Compose | http://localhost:3003 | 3003 | Mapeamento `3003:3003` (ver `docker-compose.yml`) |
| Execução local (mvn spring-boot:run) | http://localhost:3003 | 3003 | Acesso direto sem container |

> Se estiver usando a coleção Postman e executando sem Docker, ajuste a variável `base_url` para `http://localhost:3003`.

## 📚 Endpoints GraphQL

Base URL (Docker): `http://localhost:3003/graphql`  
Interface GraphiQL: `http://localhost:3003/graphiql`

### 🏥 Consultas por Paciente
| Query | Descrição |
|-------|-----------|
| `todosAtendimentosPaciente(pacienteId: ID!)` | Retorna todos os atendimentos de um paciente |
| `atendimentosFuturosPaciente(pacienteId: ID!)` | Retorna atendimentos futuros de um paciente |
| `atendimentosPassadosPaciente(pacienteId: ID!)` | Retorna atendimentos passados de um paciente |
| `atendimentosPorStatus(pacienteId: ID!, status: StatusAgendamento!)` | Filtra atendimentos por status |
| `atendimentosPorPeriodo(pacienteId: ID!, dataInicio: String!, dataFim: String!)` | Filtra atendimentos por período |

### 👨‍⚕️ Consultas por Médico
| Query | Descrição |
|-------|-----------|
| `atendimentosPorMedico(medicoId: ID!)` | Retorna todos os atendimentos de um médico |
| `atendimentosFuturosPorMedico(medicoId: ID!)` | Retorna atendimentos futuros de um médico |

### 🏥 Consultas por Hospital
| Query | Descrição |
|-------|-----------|
| `atendimentosPorHospital(hospitalId: ID!)` | Retorna todos os atendimentos de um hospital |

### 📋 Tipo HistoricoMedico

```graphql
type HistoricoMedico {
    id: ID!
    agendamentoId: ID!
    pacienteId: ID!
    hospitalId: ID!
    medicoId: ID!
    nomePaciente: String!
    nomeMedico: String!
    nomeHospital: String!
    enderecoHospital: String
    especializacao: String
    statusAgendamento: StatusAgendamento!
    dataHoraAgendamento: String!
    criadoEm: String!
    atualizadoEm: String
}
```

### 🏷️ Status de Agendamento

- `CRIADA` - Consulta criada
- `ATUALIZADA` - Consulta atualizada
- `CANCELADA` - Consulta cancelada
- `PENDENTE` - Consulta pendente

## ⚙️ Execução

### Via Docker Compose (recomendado)
```bash
docker-compose up -d historico-service historico-db kafka zookeeper
```
API disponível em: http://localhost:3003/graphql  
GraphiQL disponível em: http://localhost:3003/graphiql

### Local sem Docker
```bash
cd historico-service
./mvnw spring-boot:run
# Windows PowerShell
# .\mvnw.cmd spring-boot:run
```
API disponível em: http://localhost:3003/graphql  
GraphiQL disponível em: http://localhost:3003/graphiql

## 🧪 Testes

### Executar testes unitários
```bash
cd historico-service
./mvnw test
```

### Testar via GraphiQL (Interface Web)
1. Acesse http://localhost:3003/graphiql
2. Execute queries interativamente com autocomplete e documentação

### Exemplo de Query
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

## 🗃️ Banco de Dados
- PostgreSQL (container `historico-db`)
- Porta externa: 5435 (host) → 5432 (container)
- Schema: Auto-criação via JPA (ddl-auto: update)
- Banco: `historico`

## 📦 Dependências principais
- Spring Boot 3
- Spring Data JPA / Hibernate
- Spring GraphQL
- Spring Kafka (consumer)
- PostgreSQL Driver
- Resilience4j (Circuit Breaker)
- Lombok

## 🧩 Estrutura (alto nível)
```
core/
  controller/ (GraphQL controllers)
  service/ (lógica de negócio)
  entity/ (entidades JPA)
  dto/ (Data Transfer Objects)
  repository/ (interfaces JPA)
  consumer/ (Kafka consumers)
  enums/ (enumerações)
configuration/
  GraphQLConfig
  kafka/KafkaConfig
```

### GraphiQL (Recomendado)
Interface interativa disponível em: `http://localhost:3003/graphiql`

Recursos:
- ✨ Autocomplete
- 📖 Documentação inline
- 🔍 Exploração do schema
- 🧪 Teste de queries

### Schema
O schema GraphQL completo está disponível em: `src/main/resources/graphql/schema.graphqls`

---
> Referência cruzada: documentação global do projeto no README raiz.

