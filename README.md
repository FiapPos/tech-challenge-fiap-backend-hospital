# Tech Challenge FIAP - Backend Hospital

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microserviços.

> Documentação detalhada (endpoints, portas, execução) do usuario-service agora está em `usuario-service/README.md`.

## 🏗️ **ARQUITETURA**

### Microserviços:
- **usuario-service** - Gerenciamento de usuários, autenticação e autorização
- **agendamento-service** - Agendamento de consultas
- **appointment-service** - Gestão de consultas
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

---
