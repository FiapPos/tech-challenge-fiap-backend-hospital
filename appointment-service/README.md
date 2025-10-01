# Appointment Service - Authentication & Authorization

Microserviço responsável pela **autenticação e autorização** de usuários do sistema hospitalar, refatorado a partir do appointment-service original para focar exclusivamente em funcionalidades de segurança.

## 📋 Funcionalidades Principais

- **Autenticação JWT**: Login seguro com geração de tokens JWT
- **Autorização baseada em perfil**: Controle de acesso por perfil único (ADMIN, MEDICO, ENFERMEIRO, PACIENTE)
- **Validação de tokens**: Verificação da validade dos tokens JWT para outros microserviços
- **Integração com Kafka**: Comunicação assíncrona sobre eventos de autenticação
- **API REST**: Endpoints padronizados para autenticação e autorização

## 🏗️ Arquitetura

appointment-service/
├── src/main/java/com/fiap/techchallenge/appointment_service/
│   ├── core/
│   │   ├── controller/          # AuthController (ex-LoginController)
│   │   ├── service/             # Auth, JWT, UserDetails services
│   │   ├── repository/          # Usuario e Perfil repositories
│   │   ├── domain/entities/     # Usuario, Perfil (relacionamento 1:1)
│   │   └── dto/                 # LoginRequest, AuthResponse, UserDetailsImpl
│   └── config/                  # Security, JWT, Exception, Kafka configs
└── src/main/resources/
    ├── application.yml          # Configurações atualizadas
    └── data.sql                # Script de inicialização
```

## 🛠️ Tecnologias

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security** - Segurança e autenticação
- **JWT (JSON Web Token)** - Tokens de autenticação
- **PostgreSQL** - Banco de dados relacional
- **Apache Kafka** - Mensageria para eventos de auth
- **Docker** - Containerização
- **Lombok** - Redução de boilerplate

## 🚀 Endpoints

### Autenticação
```http
POST /api/auth/login
```
**Body:**
```json
{
  "login": "admin",
  "senha": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 1,
  "login": "admin",
  "nome": "Administrador",
  "email": "admin@hospital.com",
  "perfil": "ADMIN",
  "expiresAt": "2025-09-29T10:30:00"
}
```

### Validação de Token
```http
POST /api/auth/validate?token=eyJhbGciOiJIUzI1NiJ9...
```

### Obter Usuário do Token
```http
GET /api/auth/user?token=eyJhbGciOiJIUzI1NiJ9...
```

## 👥 Perfis de Usuário

> **⚠️ Regra Importante**: Cada usuário pode ter apenas **um perfil** associado. Esta regra garante simplicidade na autorização e evita conflitos de permissões.

| Perfil | Descrição | Permissões |
|--------|-----------|------------|
| **ADMIN** | Administrador do sistema | Acesso total |
| **MEDICO** | Médico | Acesso completo aos pacientes |
| **ENFERMEIRO** | Enfermeiro | Acesso aos cuidados dos pacientes |
| **PACIENTE** | Paciente | Acesso aos próprios dados |

## 🗄️ Modelo de Dados

### Entidades Principais

- **Usuario**: Dados do usuário (login, senha, nome, email, etc.)
- **Perfil**: Perfil de acesso do sistema (cada usuário possui apenas um perfil)

## ⚙️ Configuração

### Variáveis de Ambiente

```yaml
# Banco de dados
DB_USER=postgres
DB_PASSWORD=postgres

# JWT
JWT_SECRET=mySecretKeyForHospitalAuthenticationService
JWT_EXPIRATION=86400

# Kafka
KAFKA_BROKER=localhost:9092
```

### Porta do Serviço
- **Porta**: 3002

## 🔒 Segurança

- **Senhas criptografadas** com BCrypt
- **Tokens JWT** com assinatura HMAC
- **Validação de entrada** com Bean Validation
- **CORS configurado** para ambiente de produção
- **Endpoints públicos** apenas para login e validação

## 🧪 Usuário Padrão

O sistema vem com um usuário administrador pré-configurado:

- **Login**: `admin`
- **Senha**: `admin123`
- **Perfil**: `ADMIN`

## 📊 Monitoramento

### Actuator Endpoints
- `/actuator/health` - Status da aplicação
- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas da aplicação

## 🔗 Integração com outros Microserviços

O appointment-service se integra com outros serviços através de:

1. **Tokens JWT**: Outros serviços validam tokens através do endpoint `/api/auth/validate`
2. **Kafka Events**: Publica eventos de autenticação nos tópicos:
   - `user-authenticated`: Eventos de login bem-sucedido
   - `user-authorization`: Eventos de autorização
3. **API REST**: Outros serviços podem consultar informações de usuários

### ✅ **Adicionado:**
- **AuthController** - Novo controlador de autenticação
- **AuthService**, **JwtService** - Serviços de autenticação
- **Usuario**, **Perfil** - Entidades de usuário (relacionamento 1:1)
- **UserDetailsImpl** - Implementação Spring Security
- **SecurityConfig** - Configuração de segurança
- **JWT Authentication Filter** - Filtro de autenticação
- **Tratamento de exceções** específico para auth

### 🔄 **Refatorado:**
- **LoginController** → **AuthController** - Totalmente reescrito
- **application.yml** - Configurações atualizadas para auth
- **KafkaConfig** - Tópicos alterados para eventos de auth
- **Exception handling** - Melhorado para auth

## 🚨 Tratamento de Exceções

- **ValidationException**: Erros de validação (400)
- **BadCredentialsException**: Credenciais inválidas (401)
- **UsernameNotFoundException**: Usuário não encontrado (401)
- **MethodArgumentNotValidException**: Erro de validação de campos (400)

---

**Importante**: Este microserviço foi **completamente refatorado** de um sistema de pedidos para um sistema de autenticação/autorização, mantendo os padrões arquiteturais originais mas focando exclusivamente em segurança e controle de acesso do sistema hospitalar.