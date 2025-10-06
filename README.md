# Tech Challenge FIAP - Backend Hospital

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microserviços.

## 🏗️ **ARQUITETURA**

### Microserviços:
- **usuario-service** - Gerenciamento de usuários, autenticação e autorização
- **agendamento-service** - Agendamento de consultas
- **appointment-service** - Gestão de consultas
- **hospital-service** - Gerenciamento de hospitais
- **historico-service** - Histórico médico
- **notificacao-service** - Notificações
- **orchestrator-service** - Orquestração de transações

### Tecnologias:
- **Backend:** Java 17, Spring Boot 3.5.5, Spring Security
- **Database:** PostgreSQL 17.6
- **ORM:** JPA/Hibernate 6.6.26
- **Messaging:** Apache Kafka
- **Authentication:** JWT
- **Containerization:** Docker

### Documentações
- **agendamento-service**
  - [Documentação](agendamento-service/README.md)
- **appointment-service**
- **historico-service**
  - [Documentação](historico-service/README.md)
  - [Guia de testes](historico-service/GUIA_TESTES_GRAPHQL.md)
- **hospital-service**
  - [README.md](hospital-service/README.md)
- **notificacao-service**
  - [Documentação](notificacao-service/README.md)
- **orchestrator-service**
  - [Documentação](orchestrator-service/README.md)
- **usuario-service**
  - [Documentação](usuario-service/README.md)
  - [Informações para o postman](usuario-service/postman)

---
