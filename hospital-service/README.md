# Hospital Service

Serviço responsável pelo gerenciamento de hospitais (cadastro, consulta, atualização e listagem).

## 🔌 Portas

| Contexto | URL Base | Porta Interna (Spring) | Observação |
|----------|----------|------------------------|------------|
| Docker Compose | http://localhost:3006 | 3000 | Mapeamento `3006:3000` (ver `docker-compose.yml`) |
| Execução local (mvn spring-boot:run) | http://localhost:3000 | 3000 | Acesso direto sem container |

> Se estiver usando ferramentas como Postman e executando sem Docker, ajuste a `base_url` para `http://localhost:3000`.

## 📚 Endpoints

Base URL (Docker): `http://localhost:3006`

### 🏥 Hospitais
| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | /api/hospitais | Criar novo hospital |
| GET | /api/hospitais | Listar todos os hospitais |
| GET | /api/hospitais/{id} | Buscar hospital por ID |
| PUT | /api/hospitais/{id} | Atualizar hospital |

### 📋 Estrutura do Hospital

```json
{
  "id": 1,
  "nome": "Hospital São Paulo",
  "endereco": "Rua Exemplo, 123 - São Paulo, SP",
  "telefone": "11987654321",
  "email": "contato@hospitalsaopaulo.com.br",
  "especialidades": "Cardiologia, Neurologia, Ortopedia",
  "ativo": true,
  "criadoEm": "2024-10-01T10:30:00",
  "atualizadoEm": "2024-10-05T14:20:00"
}
```

### 📝 Exemplo de Request (POST/PUT)

```json
{
  "nome": "Hospital São Paulo",
  "endereco": "Rua Exemplo, 123 - São Paulo, SP",
  "telefone": "(11) 98765-4321",
  "email": "contato@hospitalsaopaulo.com.br",
  "especialidades": "Cardiologia, Neurologia, Ortopedia"
}
```

## ⚙️ Execução

### Via Docker Compose (recomendado)
```bash
docker-compose up -d hospital-service hospital-db
```
API disponível em: http://localhost:3006

### Local sem Docker
```bash
cd hospital-service
./mvnw spring-boot:run
# Windows PowerShell
# .\mvnw.cmd spring-boot:run
```
API disponível em: http://localhost:3000

## 🧪 Testes

### Executar testes unitários
```bash
cd hospital-service
./mvnw test
```

### Testar via cURL

#### Criar hospital
```bash
curl -X POST http://localhost:3006/api/hospitais \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Hospital São Paulo",
    "endereco": "Rua Exemplo, 123 - São Paulo, SP",
    "telefone": "(11) 98765-4321",
    "email": "contato@hospitalsaopaulo.com.br",
    "especialidades": "Cardiologia, Neurologia"
  }'
```

#### Listar hospitais
```bash
curl http://localhost:3006/api/hospitais
```

#### Buscar hospital por ID
```bash
curl http://localhost:3006/api/hospitais/1
```

#### Atualizar hospital
```bash
curl -X PUT http://localhost:3006/api/hospitais/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Hospital São Paulo - Unidade Centro",
    "endereco": "Rua Nova, 456 - São Paulo, SP",
    "telefone": "(11) 98765-4321",
    "email": "contato@hospitalsaopaulo.com.br",
    "especialidades": "Cardiologia, Neurologia, Ortopedia"
  }'
```

## 🗃️ Banco de Dados
- PostgreSQL (container `hospital-db`)
- Porta externa: 5436 (host) → 5432 (container)
- Banco: `hospitais`

## 📦 Dependências principais
- Spring Boot 3
- Spring Data JPA / Hibernate
- Spring Validation
- Spring Kafka (integração futura)
- PostgreSQL Driver
- Lombok

## 🧩 Estrutura (alto nível)
```
core/
  controller/ (REST controllers)
  service/ (lógica de negócio)
  entity/ (entidades JPA)
  dto/ (Data Transfer Objects)
    HospitalRequest
    HospitalResponse
  repository/ (interfaces JPA)
  mapper/ (conversão entre entidades e DTOs)
  exception/ (tratamento de exceções)
```

## 🎨 Padrões de Projeto

### DTO Pattern
Separação clara entre entidades de domínio e objetos de transferência:
- `HospitalRequest` - Dados de entrada (criação/atualização)
- `HospitalResponse` - Dados de saída (respostas da API)
- `Hospital` - Entidade de domínio/JPA

### Mapper Pattern
`HospitalMapper` - Converte entre entidades e DTOs de forma centralizada

### Exception Handler
`GlobalExceptionHandler` - Tratamento centralizado de exceções com respostas padronizadas

## 📄 Validações

O serviço valida:
- ✅ Nome do hospital (obrigatório, máx. 200 caracteres)
- ✅ Endereço (obrigatório, máx. 300 caracteres)
- ✅ Telefone (obrigatório, máx. 20 caracteres)
- ✅ Email (opcional, máx. 100 caracteres)
- ✅ Especialidades (opcional, texto livre)

---
> Referência cruzada: documentação global do projeto no README raiz.

