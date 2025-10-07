# Tech Challenge FIAP - Backend Hospital

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microserviços.

## Links úteis
- Github: https://github.com/FiapPos/tech-challenge-fiap-backend-hospital
- Collection do Postman: [Appointment Service API.postman_collection.json](appointment-service/Appointment%20Service%20API.postman_collection.json)
- Documentação para testes: [README-POSTMAN.md](appointment-service/README-POSTMAN.md)

## Equipe
- [Gustavo Lima Aliba](https://github.com/GustavoLimaAl)
- [Julio Cesar Salerno da Silva](https://github.com/jcsalerno)
- [Sonia Alves Ribeiro](https://github.com/hopesoh)
- [Stephanie Ingrid Menezes](https://github.com/steingcam)

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

### Portas
#### 🌐 ACESSANDO DE FORA DO DOCKER (Navegador, Postman, curl)
```
┌─────────────────────────┬──────────────────────────────────────┐
│ Serviço                 │ URL Correta                          │
├─────────────────────────┼──────────────────────────────────────┤
│ usuario-service         │ http://localhost:3001                │
│ agendamento-service     │ http://localhost:3002                │
│ historico-service       │ http://localhost:3003                │
│ notificacao-service     │ http://localhost:3004                │
│ appointment-service     │ http://localhost:3005                │
│ hospital-service        │ http://localhost:3006                │
│ orchestrator-service    │ http://localhost:8080                │
│ redpanda-console        │ http://localhost:8081                │
└─────────────────────────┴──────────────────────────────────────┘
```

#### 🐳 ACESSANDO DE DENTRO DO DOCKER (Container → Container)
```
┌─────────────────────────┬──────────────────────────────────────┐
│ Serviço                 │ URL Correta                          │
├─────────────────────────┼──────────────────────────────────────┤
│ usuario-service         │ http://usuario-service:3000          │
│ agendamento-service     │ http://agendamento-service:3000      │
│ historico-service       │ http://historico-service:3003        │
│ notificacao-service     │ http://notificacao-service:3000      │
│ appointment-service     │ http://appointment-service:3002      │
│ hospital-service        │ http://hospital-service:3000         │
│ orchestrator-service    │ http://orchestrator-service:8080     │
└─────────────────────────┴──────────────────────────────────────┘
```

#### 🗄️ BANCOS DE DADOS (Acesso Externo - pgAdmin, DBeaver)
```
┌─────────────────────────┬──────────────────────────────────────┐
│ Banco de Dados          │ Conexão                              │
├─────────────────────────┼──────────────────────────────────────┤
│ appointment-db          │ localhost:5432                       │
│ usuario-db              │ localhost:5433                       │
│ agendamento-db          │ localhost:5434                       │
│ historico-db            │ localhost:5435                       │
│ hospital-db             │ localhost:5436                       │
└─────────────────────────┴──────────────────────────────────────┘
```

#### 📡 INFRAESTRUTURA
```
┌─────────────────────────┬──────────────────────────────────────┐
│ Serviço                 │ URL/Porta                            │
├─────────────────────────┼──────────────────────────────────────┤
│ Kafka (externo)         │ localhost:9092                       │
│ Kafka (interno)         │ kafka:29092                          │
│ Zookeeper               │ localhost:2181                       │
│ Redpanda Console        │ http://localhost:8081                │
└─────────────────────────┴──────────────────────────────────────┘
```

### Documentações
- **agendamento-service**
  - [Documentação](agendamento-service/README.md)
- **appointment-service**
  - [Documentação](appointment-service/README.md)
  - [Documentação do postman](appointment-service/README-POSTMAN.md)
- **historico-service**
  - [Documentação](historico-service/README.md)
  - [Guia de testes](historico-service/GUIA_TESTES_GRAPHQL.md)
- **hospital-service**
  - [Documentação](hospital-service/README.md)
- **notificacao-service**
  - [Documentação](notificacao-service/README.md)
- **orchestrator-service**
  - [Documentação](orchestrator-service/README.md)
- **usuario-service**
  - [Documentação](usuario-service/README.md)
  - [Informações para o postman](usuario-service/postman)

---
