# Agendamento Service

Serviço responsável por registrar, atualizar, confirmar e cancelar consultas médicas.

## 🔌 Portas

| Contexto | URL Base | Porta Interna (Spring) | Observação |
|----------|----------|------------------------|------------|
| Docker Compose | http://localhost:3002 | 3000 | Mapeamento `3002:3000` (ver `docker-compose.yml`) |
| Execução local (mvn spring-boot:run) | http://localhost:3000 | 3000 | Acesso direto sem container |

> Se estiver usando ferramentas como Postman e executando sem Docker, ajuste a `base_url` para `http://localhost:3000`.

## 📚 Endpoints

Base URL (Docker): `http://localhost:3002`

### 📅 Consultas
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /api/consultas | Criar nova consulta (status PENDENTE) |
| GET | /api/consultas/{id} | Buscar consulta por ID |
| PUT | /api/consultas/{id} | Atualizar consulta |
| PUT | /api/consultas/confirmar | Confirmar consulta |
| DELETE | /api/consultas/cancelar | Cancelar consulta |

### 📋 Estrutura da Consulta

```json
{
  "agendamentoId": 1,
  "pacienteId": 123,
  "medicoId": 456,
  "especialidadeId": 789,
  "hospitalId": 101,
  "nomePaciente": "João Silva",
  "nomeMedico": "Dr. Carlos Santos",
  "nomeHospital": "Hospital São Paulo",
  "enderecoHospital": "Rua Exemplo, 123 - São Paulo, SP",
  "especializacao": "Cardiologia",
  "statusAgendamento": "PENDENTE",
  "dataHoraAgendamento": "2024-12-15T14:30:00",
  "criadoEm": "2024-10-01T10:30:00",
  "atualizadoEm": "2024-10-05T14:20:00",
  "observacoes": "Paciente relatou dores no peito"
}
```

### 🏷️ Status de Agendamento

- `PENDENTE` - Consulta aguardando confirmação
- `CRIADA` - Consulta confirmada e criada
- `ATUALIZADA` - Consulta alterada
- `CANCELADA` - Consulta cancelada

### 📝 Exemplo de Request (POST)

```json
{
  "pacienteId": 123,
  "medicoId": 456,
  "especialidadeId": 789,
  "hospitalId": 101,
  "nomePaciente": "João Silva",
  "nomeMedico": "Dr. Carlos Santos",
  "nomeHospital": "Hospital São Paulo",
  "enderecoHospital": "Rua Exemplo, 123 - São Paulo, SP",
  "especializacao": "Cardiologia",
  "dataHoraAgendamento": "2024-12-15T14:30:00",
  "observacoes": "Paciente relatou dores no peito"
}
```

## ⚙️ Execução

### Via Docker Compose (recomendado)
```bash
docker-compose up -d agendamento-service agendamento-db kafka zookeeper
```
API disponível em: http://localhost:3002

### Local sem Docker
```bash
cd agendamento-service
./mvnw spring-boot:run
# Windows PowerShell
# .\mvnw.cmd spring-boot:run
```
API disponível em: http://localhost:3000

## 🧪 Testes

### Executar testes unitários
```bash
cd agendamento-service
./mvnw test
```

### Testar via cURL

#### Criar consulta pendente
```bash
curl -X POST http://localhost:3002/api/consultas \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteId": 123,
    "medicoId": 456,
    "especialidadeId": 789,
    "hospitalId": 101,
    "nomePaciente": "João Silva",
    "nomeMedico": "Dr. Carlos Santos",
    "nomeHospital": "Hospital São Paulo",
    "enderecoHospital": "Rua Exemplo, 123",
    "especializacao": "Cardiologia",
    "dataHoraAgendamento": "2024-12-15T14:30:00"
  }'
```

#### Buscar consulta por ID
```bash
curl http://localhost:3002/api/consultas/1
```

#### Confirmar consulta
```bash
curl -X PUT http://localhost:3002/api/consultas/confirmar \
  -H "Content-Type: application/json" \
  -d '{
    "agendamentoId": 1,
    "pacienteId": 123,
    "medicoId": 456,
    "especialidadeId": 789,
    "hospitalId": 101
  }'
```

#### Atualizar consulta
```bash
curl -X PUT http://localhost:3002/api/consultas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "medicoId": 789,
    "especialidadeId": 456,
    "hospitalId": 101,
    "dataHoraAgendamento": "2024-12-20T10:00:00"
  }'
```

#### Cancelar consulta
```bash
curl -X DELETE http://localhost:3002/api/consultas/cancelar \
  -H "Content-Type: application/json" \
  -d '{
    "agendamentoId": 1
  }'
```

## 🗃️ Banco de Dados
- PostgreSQL (container `agendamento-db`)
- Porta externa: 5434 (host) → 5432 (container)
- Banco: `agendamentos`

## 📦 Dependências principais
- Spring Boot 3
- Spring Data JPA / Hibernate
- Spring Validation
- Spring Kafka (producer e consumer)
- Resilience4j (Circuit Breaker)
- OpenFeign (chamadas HTTP)
- PostgreSQL Driver
- Lombok

## 🧩 Estrutura (alto nível)
```
core/
  controller/ (REST controllers)
  service/ (lógica de negócio)
  entity/ (entidades JPA)
    Consulta
  dto/ (Data Transfer Objects)
    DadosAgendamento
  repository/ (interfaces JPA)
  producer/ (Kafka producers)
  enums/ (enumerações)
    EStatusAgendamento
configuration/
  kafka/KafkaConfig
```

## 🔄 Integração Kafka

O serviço publica e consome eventos do Kafka para orquestração de consultas:

### Topics de Publicação
- `notificacao-sucesso` - Envia dados para o serviço de notificação
- `consultas` - Eventos de consultas para outros serviços
- `historico-sucesso` - Envia dados para o serviço de histórico

### Consumer
- **Consumer Group**: `agendamento-group`
- Processa eventos de outros serviços relacionados a agendamentos

## 📄 Fluxo de Criação de Consulta

1. **POST /api/consultas** - Cria consulta com status `PENDENTE`
2. **Publica evento Kafka** - Notifica outros serviços
3. **Aguarda confirmação** - Orquestrador valida disponibilidade
4. **PUT /api/consultas/confirmar** - Confirma consulta (status `CRIADA`)
5. **Notifica paciente** - Via serviço de notificação
6. **Registra histórico** - Via serviço de histórico

---
> Referência cruzada: documentação global do projeto no README raiz.

