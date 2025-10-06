# Orchestrator Service

Serviço responsável por orquestrar a comunicação assíncrona entre microserviços, garantindo a execução da saga de agendamento de consultas médicas.

## 🔌 Portas

| Contexto | URL Base | Porta Interna (Spring) | Observação |
|----------|----------|------------------------|------------|
| Docker Compose | http://localhost:8080 | 8080 | Mapeamento `8080:8080` (ver `docker-compose.yml`) |
| Execução local (mvn spring-boot:run) | http://localhost:8080 | 8080 | Acesso direto sem container |

> Se estiver usando ferramentas como Postman e executando sem Docker, ajuste a `base_url` para `http://localhost:8080`.

## 📚 Endpoints

Base URL (Docker): `http://localhost:8080`

### 🔄 Saga de Agendamentos
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /api/saga/agendamentos | Iniciar saga de criação de agendamento |
| PUT | /api/saga/agendamentos/{agendamentoId} | Editar agendamento (saga de atualização) |
| PUT | /api/saga/agendamentos/{agendamentoId}/cancelar | Cancelar agendamento (saga de cancelamento) |

### 📋 Estrutura do Request (POST)

```json
{
  "pacienteId": 123,
  "medicoId": 456,
  "especialidadeId": 789,
  "hospitalId": 101,
  "dataHora": "2024-12-15T14:30:00"
}
```

### 📋 Estrutura do Request (PUT)

```json
{
  "medicoId": 789,
  "especialidadeId": 456,
  "hospitalId": 102,
  "dataHora": "2024-12-20T10:00:00"
}
```

### 📋 Estrutura da Response

```json
{
  "sucesso": true,
  "mensagem": "Agendamento concluído com sucesso.",
  "agendamentoId": 1
}
```

## ⚙️ Execução

### Via Docker Compose (recomendado)
```bash
docker-compose up --build
```
API disponível em: http://localhost:8080

## 🧪 Testes

### Testar via cURL

#### Criar agendamento (saga completa)
```bash
curl -X POST http://localhost:8080/api/saga/agendamentos \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteId": 123,
    "medicoId": 456,
    "especialidadeId": 789,
    "hospitalId": 101,
    "dataHora": "2024-12-15T14:30:00"
  }'
```

#### Editar agendamento
```bash
curl -X PUT http://localhost:8080/api/saga/agendamentos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "medicoId": 789,
    "especialidadeId": 456,
    "hospitalId": 102,
    "dataHora": "2024-12-20T10:00:00"
  }'
```

#### Cancelar agendamento
```bash
curl -X PUT http://localhost:8080/api/saga/agendamentos/1/cancelar
```

## 🗃️ Banco de Dados
- **Nenhum** - Serviço stateless, não persiste dados
- Orquestra operações em outros microserviços

## 📦 Dependências principais
- Spring Boot 3
- Spring Cloud OpenFeign (comunicação entre serviços)
- Spring Kafka (mensageria assíncrona)
- Resilience4j (Circuit Breaker e retry)
- Spring Security
- Spring Validation
- Lombok

## 🧩 Estrutura (alto nível)
```
controller/
  AgendamentoSagaController
service/
  AgendamentoSagaService (lógica da saga)
client/ (Feign Clients)
  UsuarioServiceClient
  AgendamentoServiceClient
  HospitalServiceClient
dto/ (Data Transfer Objects)
  AgendamentoRequest
  AgendamentoUpdateRequest
  DadosAgendamento
  SagaResponse
enums/
  EStatusAgendamento
  Perfil
config/
  FeignConfig
  SecurityConfig
```

## 🔄 Padrão Saga

O serviço implementa o **Saga Pattern** (orquestrado) para garantir consistência transacional entre microserviços.

### Fluxo da Saga de Criação

```
┌─────────────────────────────────────────────────────────┐
│  1. Validar Paciente (Usuario Service)                 │
│  2. Validar Especialidade (Usuario Service)            │
│  3. Validar Médico (Usuario Service)                   │
│  4. Validar Hospital (Hospital Service)                │
│  5. Criar Consulta Pendente (Agendamento Service)      │
│  6. Confirmar Consulta (Agendamento Service)           │
│  7. Publicar eventos Kafka (Histórico + Notificação)   │
└─────────────────────────────────────────────────────────┘
```

### Compensação (Rollback)

Se qualquer etapa falhar, o orquestrador executa **ações compensatórias**:
- Cancela a consulta criada
- Reverte operações parcialmente concluídas
- Retorna erro ao cliente

## 🔗 Integração com Serviços

### Feign Clients (Comunicação Síncrona)
- `UsuarioServiceClient` → http://usuario-service:3000
- `AgendamentoServiceClient` → http://agendamento-service:3000
- `HospitalServiceClient` → http://hospital-service:3000

## 🛡️ Resiliência

### Circuit Breaker (Resilience4j)
Protege contra falhas em cascata quando serviços estão indisponíveis.

### Retry Policy
Retenta operações que falharam temporariamente.

## 🎯 Benefícios do Padrão Saga

✅ **Consistência Eventual** - Garante integridade entre serviços  
✅ **Compensação Automática** - Rollback distribuído em caso de falha  
✅ **Orquestração Centralizada** - Lógica de coordenação em um único lugar  
✅ **Rastreabilidade** - Logs detalhados de cada etapa da saga  
✅ **Tolerância a Falhas** - Circuit breaker e retry automático

---
> Referência cruzada: documentação global do projeto no README raiz.

