# Appointment Service - API Gateway

Microserviço que atua como **API Gateway** do sistema hospitalar, fornecendo um ponto único de entrada para todas as requisições externas e gerenciando autenticação, autorização e roteamento direto para os serviços especializados (usuario-service, ### 🔒 **Security Components**
- **JwtAuthenticationEntryPoint**: Ponto de entrada para autenticação
- **SecurityConfig**: Configuração de segurança Spring
- **ValidarPaciente**: Validador específico para acesso a dados de pacientes

### 📊 **GraphQL Integration**
- **Dynamic Query Builder**: Construção de queries baseada nos parâmetros de entrada
- **Response Parser**: Parser automático de respostas GraphQL para DTOs
- **Error Handling**: Tratamento específico de erros GraphQL
- **Field Selection**: Otimização de queries selecionando apenas campos necessáriosestrator-service e historico-service).

## 📋 Funcionalidades Principais

- **API Gateway**: Ponto único de entrada para clientes externos
- **Autenticação JWT**: Login seguro com geração e validação de tokens
- **Autorização baseada em perfil**: Controle de acesso por roles (ADMIN, MEDICO, ENFERMEIRO, PACIENTE)
- **Proxy Multi-Protocolo**: Encaminha requisições REST para usuario-service e orchestrator-service, e queries GraphQL para historico-service
- **Gestão de Agendamentos**: Criação e edição de agendamentos médicos
- **Histórico Médico**: Consulta de históricos de pacientes e médicos
- **Integração com Kafka**: Comunicação assíncrona para eventos do sistema

## 🏗️ Arquitetura do Sistema

```
┌─────────────────────────────────────────────────┐
│            🌐 CLIENTES EXTERNOS                  │
│     (Web, Mobile, Aplicações Terceiras)         │
└─────────────────┬───────────────────────────────┘
                  │ Todas as requisições
                  ▼
┌─────────────────────────────────────────────────┐
│           🚪 API GATEWAY                        │
│         (appointment-service:3002)              │
│                                                 │
│  ✅ Autenticação JWT                           │
│  ✅ Autorização por Perfil                     │
│  ✅ Rate Limiting                              │
│  ✅ Logging Centralizado                       │
└─────────────────┬───────────────────────────────┘
                  │ Comunicação direta
    ┌─────────────┼
    │             │             
┌───▼───┐    ┌────▼────┐   
│usuario│    │orches-  │   
│service│    │trator   │   
│ :3000 │    │service  │   
│       │    │ :8080   │   
│Login &│    │Agenda-  │   
│Auth   │    │mentos   │   
│(REST) │    │(REST)   │ 
└───────┘    └─────────┘  
```

## 🏗️ Estrutura do Projeto

```
appointment-service/
├── src/main/java/com/fiap/techchallenge/appointment_service/
│   ├── core/
│   │   ├── client/orchestrator/     # Cliente Feign para Orchestrator
│   │   ├── client/usuario/          # Cliente Feign para Usuario Service
│   │   ├── client/historico/        # Cliente GraphQL para Historico Service
│   │   ├── dto/request/            # DTOs de requisição
│   │   ├── dto/response/           # DTOs de resposta
│   │   ├── facade/                 # Facades para chamadas remotas
│   │   ├── mapper/                 # Mappers para conversão de dados
│   │   ├── service/                # Serviços de negócio
│   │   └── utils/                  # Utilitários
│   ├── infra/
│   │   ├── api/controller/         # Controllers REST
│   │   ├── client/                 # Configurações de clientes
│   │   ├── config/                 # Configurações de segurança e JWT
│   │   ├── resiliencia/            # Circuit breakers e retry
│   │   ├── security/               # Filtros e validadores de segurança
│   │   └── web/                    # Configurações web
│   └── config/                     # Configurações gerais
└── src/main/resources/
    ├── application.yml             # Configurações da aplicação
    └── data.sql                   # Scripts de inicialização
```

## 🛠️ Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security** - Segurança e autenticação
- **JWT (JSON Web Token)** - Tokens de autenticação
- **OpenFeign** - Cliente REST para comunicação síncrona
- **GraphQL** - Consultas flexíveis para histórico médico

- **Docker** - Containerização
- **Lombok** - Redução de boilerplate

## 🚀 Endpoints

### 🔐 Autenticação
```http
POST /api/auth/login
```
**Body:**
```json
{
  "login": "admin",
  "senha": "senha123",
  "perfil": "ADMIN"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 📅 Agendamentos
```http
POST /api/agendamento/criacao
```
**Headers:** `Authorization: Bearer {token}`  
**Perfis:** MEDICO, ENFERMEIRO  
**Body:**
```json
{
  "pacienteId": 102,
  "medicoId": 101,
  "hospitalId": 1,
  "especialidadeId": 1,
  "dataHora": "2025-10-15T10:30:00"
}
```

```http
PUT /api/agendamento/{id}
```
**Headers:** `Authorization: Bearer {token}`  
**Perfis:** MEDICO  
**Body:**
```json
{
  "pacienteId": 102,
  "medicoId": 101,
  "dataHora": "2025-10-15T15:30:00",
  "descricao": "Consulta de rotina - horário alterado"
}
```

## 👥 Perfis de Usuário

> **⚠️ Regra Importante**: Cada usuário pode ter apenas **um perfil** associado. Esta regra garante simplicidade na autorização e evita conflitos de permissões.

| Perfil | Descrição | Permissões |
|--------|-----------|------------|
| **ADMIN** | Administrador do sistema | Acesso total |
| **MEDICO** | Médico | Acesso completo aos pacientes |
| **ENFERMEIRO** | Enfermeiro | Acesso aos cuidados dos pacientes |
| **PACIENTE** | Paciente | Acesso aos próprios dados |

## � Fluxo de Comunicação

### 1. Requisição de Cliente
1. **Cliente** envia requisição para `appointment-service:3002`
2. **API Gateway** valida token JWT e autorização
3. **API Gateway** identifica o tipo de operação
4. **API Gateway** roteia diretamente para o serviço apropriado

### 2. Roteamento Direto
1. **Login/Auth** → `usuario-service:3000` (REST)
2. **Agendamentos** → `orchestrator-service:8080` (REST)
3. **Histórico Médico** → `historico-service:3003` (GraphQL)
4. **Resposta** retorna diretamente para o API Gateway

### 3. Componentes de Resiliência

Os componentes de resiliência são aplicados nas seguintes camadas do serviço:

#### 🔧 **Implementação Atual:**
- **Exception Mapping**: `ChamadaRemotaFacade` + `MapeadorExcecaoChamadaRemota`
  - Localização: `core/facade/ChamadaRemotaFacade.java`
  - Função: Captura e mapeia exceções de Feign e RestClient para `OrchestratorException`
  
- **Feign Client Configuration**: Configuração de timeout e retry básico
  - Connect Timeout: 5s, Read Timeout: 10s
  - Localização: `application.yml` seção `feign.client.config`


#### 📍 **Localização dos Arquivos:**
- **Facade**: `src/main/java/.../core/facade/ChamadaRemotaFacade.java`
- **Exception Mapping**: `src/main/java/.../infra/web/MapeadorExcecaoChamadaRemota.java`
- **Resiliência**: `src/main/java/.../infra/resiliencia/MapeadorExcecaoImpl.java`
- **Configuração**: `src/main/resources/application.yml`

## 🗄️ Integração com Serviços

### Comunicação Síncrona REST (Feign Client)
- **Usuario Service**: Autenticação e validação de usuários
- **Orchestrator Service**: Gestão de agendamentos e operações complexas

### Comunicação GraphQL
- **Historico Service**: Consultas de histórico médico e atendimentos via GraphQL queries

### Comunicação Assíncrona (Kafka)
- **Tópicos**: `comeca-saga`, `notifica-fim`
- **Consumer Group**: `appointment-group`
- **Eventos**: Notificações de agendamentos e mudanças de status

## ⚙️ Configuração

### Variáveis de Ambiente

```yaml
# Servidor
SERVER_PORT=3002

# JWT
JWT_SECRET=mySecretKeyForHospitalGatewayService
JWT_EXPIRATION=86400

# Kafka
KAFKA_BROKER=localhost:9092

# Serviços
ORCHESTRATOR_SERVICE_URL=http://orchestrator-service:8080
USUARIO_SERVICE_URL=http://localhost:3000
HISTORICO_SERVICE_GRAPHQL_URL=http://localhost:3003/graphql
```

### Portas dos Serviços
- **API Gateway (appointment-service)**: 3002
- **Usuario Service**: 3000
- **Orchestrator Service**: 8080
- **Historico Service**: 3003

## 🔒 Segurança

- **Senhas criptografadas** com BCrypt
- **Tokens JWT** com assinatura HMAC
- **Validação de entrada** com Bean Validation
- **CORS configurado** para ambiente de produção
- **Endpoints públicos** apenas para login e validação

## 🧪 Usuários de Teste

O sistema possui os seguintes usuários pré-configurados para teste:

| ID  | Nome               | CPF         | Login      | Senha    | Perfil     |
|-----|-------------------|-------------|------------|----------|------------|
| 101 | Medico Exemplo    | 11111111111 | medico     | senha123 | MEDICO     |
| 102 | Paciente Exemplo  | 22222222222 | paciente   | senha123 | PACIENTE   |
| 103 | Enfermeiro        | 33333333333 | enfermeiro | senha123 | ENFERMEIRO |

## 📊 Monitoramento

### Actuator Endpoints
- `/actuator/health` - Status da aplicação
- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas da aplicação

## 🔗 Componentes Arquiteturais

### 🚪 **API Gateway (appointment-service)**
**Responsabilidades:**
- ✅ **Autenticação**: Validação de tokens JWT
- ✅ **Autorização**: Controle de acesso por perfil
- ✅ **Rate Limiting**: Controle de taxa de requisições
- ✅ **Logging**: Registro de todas as requisições
- ✅ **CORS**: Configuração de Cross-Origin
- ✅ **Headers Injection**: Adiciona `X-User-Profile`, `X-Username`

### 🎯 **Multi-Protocol Integration**
**Funcionalidades:**
- ✅ **Roteamento Inteligente**: REST para auth/agendamentos, GraphQL para histórico
- ✅ **Circuit Breaker**: Proteção contra falhas de cada serviço/protocolo
- ✅ **Retry Logic**: Tentativas automáticas adaptadas por protocolo
- ✅ **Load Balancing**: Suporte a múltiplas instâncias com balanceamento por tipo

### 📡 **Clientes de Integração**

**REST Clients (Feign):**
- **OrchestratorFeignClient**: Cliente REST para agendamentos
- **UsuarioFeignClient**: Cliente REST para autenticação
- **Configuração de Timeout**: Connect: 5s, Read: 10s
- **Logging**: Nível básico para debug de requisições

**GraphQL Client:**
- **HistoricoGraphQLClient**: Cliente GraphQL para histórico médico
- **Query Builder**: Construção dinâmica de queries GraphQL
- **Response Mapping**: Mapeamento automático de respostas GraphQL

### � **Security Components**
- **JwtAuthenticationEntryPoint**: Ponto de entrada para autenticação
- **SecurityConfig**: Configuração de segurança Spring
- **ValidarPaciente**: Validador específico para acesso a dados de pacientes

## 🚨 Tratamento de Exceções

- **OrchestratorException**: Erros de comunicação com orchestrator (500)
- **ValidationException**: Erros de validação de entrada (400)
- **AccessDeniedException**: Acesso negado por falta de permissão (403)
- **JwtException**: Problemas com token JWT (401)
- **MethodArgumentNotValidException**: Erro de validação de campos (400)

## 📊 Monitoramento e Observabilidade

### Logs Estruturados
- **Nível DEBUG**: Comunicação com orchestrator e detalhes de autenticação
- **Nível INFO**: Requisições de entrada e respostas
- **Nível ERROR**: Falhas de comunicação e erros de sistema

### Métricas Disponíveis
- **Circuit Breaker**: Status e métricas de falhas
- **Resilience4j**: Métricas de retry e rate limiting
- **Feign Client**: Métricas de requisições HTTP

### Health Checks
- `/actuator/health`: Status geral da aplicação
- `/actuator/info`: Informações da aplicação
- `/actuator/metrics`: Métricas detalhadas

## 🧪 Testando a API

### Coleção Postman
Uma coleção completa do Postman está disponível no arquivo:
- `Appointment Service API.postman_collection.json`
- `README-POSTMAN.md` (documentação da coleção)

### Fluxo de Teste Recomendado
1. **Login**: Execute login com um dos usuários de teste
2. **Token**: O token JWT será salvo automaticamente
3. **Agendamentos**: Teste criação e edição (conforme permissões)
4. **Histórico**: Consulte históricos de pacientes e médicos

---

**Importante**: Este microserviço atua como **API Gateway** do sistema hospitalar, fornecendo um ponto único e seguro de entrada para todas as requisições externas, com roteamento multi-protocolo: **REST** para usuario-service (autenticação) e orchestrator-service (agendamentos), e **GraphQL** para historico-service (consultas flexíveis de histórico médico).