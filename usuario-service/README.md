# Usuario Service

Serviço responsável pelo ciclo de vida de usuários (cadastro, atualização, desativação), perfis e relacionamento com especialidades e endereços.

## 🔌 Portas

| Contexto | URL Base | Porta Interna (Spring) | Observação |
|----------|----------|------------------------|------------|
| Docker Compose | http://localhost:3001 | 3000 | Mapeamento `3001:3000` (ver `docker-compose.yml`) |
| Execução local (mvn spring-boot:run) | http://localhost:3000 | 3000 | Acesso direto sem container |

> Se estiver usando a coleção Postman e executando sem Docker, ajuste a variável `base_url` para `http://localhost:3000`.

## 📚 Endpoints

Base URL (Docker): `http://localhost:3001`

### � Autenticação
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /login | Autenticar usuário e obter token JWT |
| PUT | /login/atualiza-senha | Atualizar senha do usuário |

### �👥 Usuários
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /usuarios | Criar novo usuário |
| GET | /usuarios | Listar usuários (filtros opcionais) |
| GET | /usuarios/por-especialidade/{especialidadeId} | Listar usuários por especialidade |
| GET | /usuarios/por-login/{login} | Buscar usuário por login |
| PUT | /usuarios/{id} | Atualizar usuário |
| DELETE | /usuarios/{id} | Desativar usuário |

### 🏥 Especialidades
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /especialidades | Criar especialidade |
| GET | /especialidades | Listar especialidades |
| PATCH | /especialidades/{id} | Atualizar especialidade |
| PATCH | /especialidades/{id}/inativar | Inativar especialidade |

### 👨‍⚕️ Especialidades do Médico
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /medicos/{medicoId}/especialidades/{especialidadeId} | Associar especialidade ao médico |
| GET | /medicos/{medicoId}/especialidades | Listar especialidades do médico |
| DELETE | /medicos/{medicoId}/especialidades/{especialidadeId} | Desassociar especialidade do médico |

### 🏠 Endereços
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /enderecos | Criar endereço |
| PUT | /enderecos/{id} | Atualizar endereço |
| DELETE | /enderecos | Deletar endereço |
| GET | /enderecos/usuario/{id} | Listar endereços do usuário |

## ⚙️ Execução

### Via Docker Compose (recomendado)
```bash
docker-compose up -d usuario-service usuario-db
```
API disponível em: http://localhost:3001

### Local sem Docker
```bash
cd usuario-service
./mvnw spring-boot:run
# Windows PowerShell
# .\mvnw.cmd spring-boot:run
```
API disponível em: http://localhost:3000

## 🧪 Testes
```bash
cd usuario-service
./mvnw test
```

## 🗃️ Banco de Dados
- Postgres (container `usuario-db`)
- Porta externa: 5433 (host) → 5432 (container)
- Migrations: Flyway em `src/main/resources/db/migration`

## 📦 Dependências principais
- Spring Boot 3
- Spring Data JPA / Hibernate
- Spring Validation
- Springdoc OpenAPI
- Kafka (integração futura / eventos)

## 🧩 Estrutura (alto nível)
```
core/
  domain/ (entidades)
  dtos/
  queries/ & usecases/
  exceptions/
  gateways/ (interfaces de repositório)
infrastructure/
  api/controllers
  data/entities & repositories (impl)
  services/validacoes
  config/
```

## 🔐 Segurança

### Autenticação JWT
O serviço implementa autenticação baseada em **JSON Web Tokens (JWT)** com as seguintes características:

- **Endpoint de login**: `POST /login`
- **Token válido por**: 24 horas (configurável via `JWT_EXPIRATION_TIME`)
- **Header de autenticação**: `Authorization: Bearer <token>`
- **Algoritmo**: HMAC256

### Endpoints Públicos (sem autenticação)
- `/login` - Autenticação
- `/usuarios/**` - Gestão de usuários
- `/swagger-ui/**` - Documentação
- `/v3/api-docs/**` - OpenAPI

### Formato do Login
```json
{
  "login": "usuario@email.com",
  "senha": "senha123",
  "perfil": "MEDICO" // Opcional: MEDICO, PACIENTE, ADMIN
}
```

### Resposta do Login
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Configurações JWT
Variáveis de ambiente:
- `JWT_SECRET`: Chave secreta para assinatura dos tokens
- `JWT_EXPIRATION_TIME`: Tempo de expiração em milissegundos (padrão: 86400000 = 24h)

## 📄 Documentação OpenAPI
Disponível em: `http://localhost:3001/swagger-ui/index.html` (Docker) ou `http://localhost:3000/swagger-ui/index.html` (local).

## 🧭 Próximos Melhorias Sugeridas
- Adicionar testes de integração (MockMvc)
- Publicar imagem em registry
- Eventos Kafka para criação/atualização de usuário
- Implementar refresh tokens
- Adicionar rate limiting nos endpoints de autenticação

---
> Referência cruzada: documentação global do projeto no README raiz.
