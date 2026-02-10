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

### 🔐 Autenticação
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /api/auth/login | Autenticar e gerar token JWT |
| PUT | /api/auth/login/atualiza-senha | Atualizar senha do usuário logado (requer JWT) |

### 👥 Usuários
| Método | Caminho                                       | Descrição |
|--------|-----------------------------------------------|-----------|
| POST | /api/usuarios                                 | Criar novo usuário |
| GET | /api/usuarios                                     | Listar usuários (filtros opcionais) |
| GET | /api/usuarios/{id}                                 | Buscar usuário por id (requer `perfil`; `especialidadeId` opcional) |
| GET | /api/usuarios/por-especialidade/{especialidadeId} | Listar usuários por especialidade |
| GET | /api/usuarios/por-login/{login}                   | Buscar usuário por login |
| GET | /api/usuarios/por-chat/{chatId}                   | Buscar id do usuário por `chatId` (Telegram) |
| GET | /api/usuarios/{id}/qrCode                         | Gerar QR Code do usuário (PNG) |
| PUT | /api/usuarios/{id}                                | Atualizar usuário |
| PUT | /api/usuarios/atualiza-chat-id/{id}?chatId={chatId} | Vincular `chatId` (Telegram) ao usuário |
| DELETE | /api/usuarios/{id}                                | Desativar usuário |

### 🏥 Especialidades
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /api/especialidades | Criar especialidade |
| GET | /api/especialidades | Listar especialidades |
| GET | /api/especialidades/{id} | Buscar especialidade por id |
| PATCH | /api/especialidades/{id} | Atualizar especialidade |
| PATCH |/api/especialidades/{id}/inativar | Inativar especialidade |

### 👨‍⚕️ Especialidades do Médico
| Método | Caminho                                                  | Descrição |
|--------|----------------------------------------------------------|-----------|
| POST | /api/medicos/{medicoId}/especialidades/{especialidadeId} | Associar especialidade ao médico |
| GET | /api/medicos/{medicoId}/especialidades                   | Listar especialidades do médico |
| DELETE | /api/medicos/{medicoId}/especialidades/{especialidadeId} | Desassociar especialidade do médico |

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

- **Endpoint de login**: `POST /api/auth/login`
- **Token válido por**: 24 horas (configurável via `JWT_EXPIRATION_TIME`)
- **Header de autenticação**: `Authorization: Bearer <token>`
- **Algoritmo**: HMAC256

### Endpoints Públicos (sem autenticação)
- `/api/**` - API principal (autenticação, usuários, especialidades, etc.)
- `/swagger-ui/**` - Documentação
- `/v3/api-docs/**` - OpenAPI
- Observação: os endpoints de **endereços** estão em `/enderecos/**` (fora de `/api`) e **exigem autenticação** conforme configuração de segurança.

### Formato do Login
```json
{
  "login": "usuario@email.com",
  "senha": "senha123",
  "perfil": "MEDICO" // Opcional: ADMIN, MEDICO, PACIENTE, ENFERMEIRO
}
```

### Resposta do Login
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Atualização de senha
Endpoint: `PUT /api/auth/login/atualiza-senha`

Body (JSON) — mínimo 8 caracteres:
```json
{
  "senha": "novaSenha123",
  "confirmacaoSenha": "novaSenha123"
}
```

### QRCode
Endpoint: `GET /api/usuarios/{id}/qrCode`

![qrcode.png](imagens/qrcode.png)
![inicio.jpeg](imagens/inicio.jpeg)

### Configurações JWT
Variáveis de ambiente:
- `JWT_SECRET`: Chave secreta para assinatura dos tokens
- `JWT_EXPIRATION_TIME`: Tempo de expiração em milissegundos (padrão: 86400000 = 24h)

## 📄 Documentação OpenAPI
Disponível em: `http://localhost:3001/swagger-ui/index.html` (Docker) ou `http://localhost:3000/swagger-ui/index.html` (local).

---
> Referência cruzada: documentação global do projeto no README raiz.
