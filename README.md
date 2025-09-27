# Tech Challenge FIAP - Backend Hospital

Sistema de gerenciamento hospitalar desenvolvido com arquitetura de microserviços.

## 📋 **LISTA COMPLETA DE ENDPOINTS - USUARIO-SERVICE**

**Base URL:** `http://localhost:3001`

---

### 👥 **USUÁRIOS**

| Método | URL | Descrição | Autorização |
|--------|-----|-----------|-------------|
| `POST` | `/usuarios` | Criar novo usuário | - |
| `GET` | `/usuarios` | Listar usuários | Validação de permissão |
| `GET` | `/usuarios/por-especialidade/{especialidadeId}` | Listar usuários por especialidade | Público |
| `GET` | `/usuarios/por-login/{login}` | Buscar usuário por login | Público |
| `PUT` | `/usuarios/{id}` | Atualizar usuário | Próprio usuário |
| `DELETE` | `/usuarios/{id}` | Desativar usuário | Admin |

---

### 🏥 **ESPECIALIDADES**

| Método | URL | Descrição | Autorização |
|--------|-----|-----------|-------------|
| `POST` | `/especialidades` | Criar especialidade | `@PreAuthorize("hasAuthority('ADMIN')")` |
| `GET` | `/especialidades` | Listar especialidades | Público |
| `PATCH` | `/especialidades/{id}` | Atualizar especialidade | `@PreAuthorize("hasAuthority('ADMIN')")` |
| `PATCH` | `/especialidades/{id}/inativar` | Inativar especialidade | `@PreAuthorize("hasAuthority('ADMIN')")` |

---

### 👨‍⚕️ **ESPECIALIDADES DO MÉDICO**

| Método | URL | Descrição |
|--------|-----|-----------|
| `POST` | `/medicos/{medicoId}/especialidades/{especialidadeId}` | Associar especialidade ao médico |
| `GET` | `/medicos/{medicoId}/especialidades` | Listar especialidades do médico |
| `DELETE` | `/medicos/{medicoId}/especialidades/{especialidadeId}` | Desassociar especialidade do médico |

---

### 🏠 **ENDEREÇOS**

| Método | URL | Descrição | Autorização |
|--------|-----|-----------|-------------|
| `POST` | `/enderecos` | Criar endereço | Próprio usuário |
| `PUT` | `/enderecos/{id}` | Atualizar endereço | Próprio usuário |
| `DELETE` | `/enderecos` | Deletar endereço | Próprio usuário |
| `GET` | `/enderecos/usuario/{id}` | Listar endereços do usuário | Próprio usuário |

---

### 📌 **NOTAS IMPORTANTES:**

1. **Porta:** O serviço roda na porta `3001`
2. **Banco de Dados:** PostgreSQL na porta `5433`

#### Mapeamento de Portas (Interna vs Externa)
| Serviço | Porta Externa (Host) | Porta Interna (Container/App) | Observação |
|---------|----------------------|-------------------------------|------------|
| usuario-service | 3001 | 3000 | `server.port=3000` em `application.yml`; exposta como 3001 no host |
| appointment-service | 3000 | 3002 | App interno usa 3002 (ver docker-compose) |
| agendamento-service | 3002 | 3000 | |
| historico-service | 3003 | 8080 | |
| notificacao-service | 3004 | 3000 | |
| PostgreSQL (usuarios) | 5433 | 5432 | Container `usuario-db` |

Se rodar o `usuario-service` localmente via Maven (sem Docker), acesse em `http://localhost:3000`. Via Docker Compose, use `http://localhost:3001`.

---
### Pré-requisitos:
- Java 17+
- Maven
- Docker (para PostgreSQL)

### Passos:

1. **Iniciar o banco de dados:**
   ```bash
   docker-compose up -d
   ```

2. **Executar o usuario-service:**
   ```bash
   cd usuario-service
   .\mvnw.cmd spring-boot:run
   ```

3. **O sistema estará disponível em:**
   - API: `http://localhost:3001`
   - Database: `localhost:5433`

---

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
