# Feedback Service

Microserviço responsável pelo gerenciamento de feedbacks de aulas na plataforma educacional.

## 📋 Funcionalidades

### Principais Recursos

- **Criação de Feedbacks**: Estudantes podem avaliar aulas com notas de 1 a 5 e comentários
- **Feedbacks Anônimos**: Opção para enviar feedback de forma anônima
- **Categorização**: Feedbacks podem ser categorizados (Conteúdo, Didática, Material, etc.)
- **Respostas**: Administradores podem responder aos feedbacks
- **Alertas Críticos**: Feedbacks com notas baixas (≤3) geram eventos para notificação imediata
- **Métricas**: Cálculo de médias por aula, curso e professor
- **Status de Acompanhamento**: PENDENTE, EM_ANALISE, RESPONDIDO, ARQUIVADO

## 🏗️ Arquitetura

### Tecnologias

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **PostgreSQL**
- **Apache Kafka** (mensageria)
- **Spring Security**
- **Lombok**

### Estrutura de Pacotes

```
com.fiap.techchallenge.feedback
├── domain
│   ├── model (Entidades: Feedback, StatusFeedback, CategoriaFeedback)
│   └── repository (FeedbackRepository)
├── application
│   ├── dto (DTOs de request/response)
│   └── service (FeedbackService)
├── infrastructure
│   ├── config (Configurações de segurança e Kafka)
│   └── messaging (Produtores de eventos Kafka)
└── presentation
    ├── controller (FeedbackController)
    └── exception (Tratamento de erros)
```

## 📡 API Endpoints

### Feedbacks

```http
POST   /api/feedbacks
GET    /api/feedbacks/{id}
GET    /api/feedbacks/aula/{aulaId}
GET    /api/feedbacks/curso/{cursoId}
GET    /api/feedbacks/estudante/{estudanteId}
GET    /api/feedbacks/professor/{professorId}
GET    /api/feedbacks/criticos
POST   /api/feedbacks/{id}/responder
PATCH  /api/feedbacks/{id}/status
```

### Métricas

```http
GET    /api/feedbacks/aula/{aulaId}/media
GET    /api/feedbacks/curso/{cursoId}/media
GET    /api/feedbacks/professor/{professorId}/media
```

## 📝 Exemplos de Uso

### Criar Feedback

```json
POST /api/feedbacks
Headers: X-User-Id: 123

{
  "aulaId": 10,
  "cursoId": 5,
  "professorId": 7,
  "nota": 4,
  "comentario": "Excelente aula! O conteúdo foi muito bem explicado e os exemplos práticos ajudaram muito.",
  "categoria": "DIDATICA",
  "anonimo": false
}
```

### Feedback Crítico

```json
POST /api/feedbacks
Headers: X-User-Id: 456

{
  "aulaId": 15,
  "cursoId": 5,
  "professorId": 7,
  "nota": 2,
  "comentario": "A aula foi muito rápida e não consegui acompanhar o conteúdo. Seria melhor dividir em duas partes.",
  "categoria": "CONTEUDO",
  "anonimo": false
}
```

**Resultado**: Além de salvar o feedback, o sistema publica um evento `feedback.critico` no Kafka para notificação imediata.

### Responder Feedback

```json
POST /api/feedbacks/42/responder
Headers: X-User-Id: 999

{
  "resposta": "Obrigado pelo feedback! Vamos revisar o conteúdo dessa aula e criar materiais complementares para facilitar o aprendizado."
}
```

## 🔔 Eventos Kafka

### Topics Publicados

#### `feedback.criado`
Publicado sempre que um novo feedback é criado.

```json
{
  "feedbackId": 42,
  "aulaId": 10,
  "cursoId": 5,
  "estudanteId": 123,
  "professorId": 7,
  "nota": 4,
  "status": "PENDENTE",
  "categoria": "DIDATICA",
  "dataCriacao": "2025-10-19T14:30:00",
  "tipoEvento": "FEEDBACK_CRIADO"
}
```

#### `feedback.critico`
Publicado quando um feedback tem nota ≤ 3 (configurável).

```json
{
  "feedbackId": 43,
  "aulaId": 15,
  "cursoId": 5,
  "estudanteId": 456,
  "professorId": 7,
  "nota": 2,
  "status": "PENDENTE",
  "categoria": "CONTEUDO",
  "dataCriacao": "2025-10-19T15:00:00",
  "prioridade": "ALTA",
  "tipoEvento": "FEEDBACK_CRITICO"
}
```

#### `feedback.respondido`
Publicado quando um administrador responde a um feedback.

```json
{
  "feedbackId": 42,
  "aulaId": 10,
  "cursoId": 5,
  "estudanteId": 123,
  "professorId": 7,
  "nota": 4,
  "status": "RESPONDIDO",
  "respondidoPor": 999,
  "resposta": "Obrigado pelo feedback...",
  "dataCriacao": "2025-10-19T14:30:00",
  "tipoEvento": "FEEDBACK_RESPONDIDO"
}
```

## ⚙️ Configuração

### application.yml

```yaml
spring:
  application:
    name: feedback-service
  datasource:
    url: jdbc:postgresql://localhost:5432/feedback_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
  kafka:
    bootstrap-servers: localhost:9092

server:
  port: 8084

feedback:
  nota-critica: 3  # Notas <= 3 são consideradas críticas
  permitir-anonimo: true
```

## 🚀 Como Executar

### Pré-requisitos

- Java 17
- PostgreSQL
- Kafka

### Executar Localmente

```bash
# Criar banco de dados
createdb feedback_db

# Executar aplicação
./mvnw spring-boot:run
```

### Docker

```bash
docker build -t feedback-service .
docker run -p 8084:8084 feedback-service
```

## 🧪 Testes

```bash
./mvnw test
```

## 📊 Modelo de Dados

### Tabela: feedbacks

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | BIGINT | ID único do feedback |
| aula_id | BIGINT | ID da aula avaliada |
| curso_id | BIGINT | ID do curso |
| estudante_id | BIGINT | ID do estudante |
| professor_id | BIGINT | ID do professor |
| nota | INTEGER | Nota de 1 a 5 |
| comentario | TEXT | Comentário do estudante |
| status | VARCHAR | Status do feedback |
| categoria | VARCHAR | Categoria do feedback |
| anonimo | BOOLEAN | Se é anônimo |
| data_criacao | TIMESTAMP | Data de criação |
| data_atualizacao | TIMESTAMP | Data da última atualização |
| respondido_por | BIGINT | ID do administrador que respondeu |
| resposta | TEXT | Resposta ao feedback |
| data_resposta | TIMESTAMP | Data da resposta |

## 🔐 Segurança

- **Autenticação**: Baseada em JWT (header `X-User-Id`)
- **Autorização**: Estudantes podem criar feedbacks, administradores podem responder
- **Anonimato**: Feedbacks anônimos não expõem o ID do estudante nas consultas

## 📈 Métricas e Monitoramento

O serviço calcula automaticamente:

- Média de notas por aula
- Média de notas por curso
- Média de notas por professor
- Contagem de feedbacks críticos

## 🔄 Integração com Outros Serviços

### Eventos Consumidos

Este serviço não consome eventos (apenas produz).

### Eventos Produzidos

- **notificacao-service**: Consome `feedback.critico` para enviar alertas
- **relatorio-service**: Consome todos os eventos de feedback para análises
- **aula-service**: Pode consumir feedbacks para atualizar métricas de aulas

## 📝 Notas Importantes

1. **Feedbacks Críticos**: Notas ≤ 3 geram notificações automáticas
2. **Validações**: Comentários devem ter entre 10 e 1000 caracteres
3. **Anonimato**: Quando ativado, o `estudanteId` não é retornado nas consultas
4. **Status**: O status padrão é `PENDENTE` na criação

## 🤝 Contribuindo

Este microserviço faz parte da plataforma educacional FIAP Tech Challenge.
spring:
  application:
    name: feedback-service
  
  datasource:
    url: jdbc:postgresql://localhost:5432/feedback_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: feedback-service-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "*"
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

server:
  port: 8084

jwt:
  secret: ${JWT_SECRET:my-secret-key-for-jwt-token-generation-and-validation-minimum-256-bits}
  expiration: 86400000

feedback:
  nota-critica: 3
  permitir-anonimo: false

