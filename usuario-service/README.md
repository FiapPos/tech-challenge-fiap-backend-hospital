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

### 👥 Usuários
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
Atualmente permissiva (endpoints públicos). Camada preparada para futura ativação de autenticação/JWT.

## 📄 Documentação OpenAPI
Disponível em: `http://localhost:3001/swagger-ui/index.html` (Docker) ou `http://localhost:3000/swagger-ui/index.html` (local).

## 🧭 Próximos Melhorias Sugeridas
- Adicionar testes de integração (MockMvc)
- Introduzir autenticação/JWT
- Publicar imagem em registry
- Eventos Kafka para criação/atualização de usuário

---
> Referência cruzada: documentação global do projeto no README raiz.
