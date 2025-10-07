# 🏗️ Arquitetura do API Gateway - Sistema Hospitalar

## 📋 Visão Geral

O **API Gateway** (appointment-service) atua como **porta única de entrada** para todas as requisições externas, comunicando-se o **orchestrator-service**, **usuario-service** e **historico-service**.

## 🔄 Fluxo de Comunicação

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
                  │ ÚNICA comunicação
                  ▼
┌─────────────────────────────────────────────────┐
│          🎯 ORCHESTRATOR SERVICE                │
│            (orchestrator:8080)                  │
│                                                 │
│  ✅ Roteamento Inteligente                     │
│  ✅ Load Balancing                             │
│  ✅ Circuit Breaker                            │
│  ✅ Service Discovery                          │
└─────────────────┬───────────────────────────────┘
                  │ Distribui para microserviços
    ┌─────────────┼─────────────┐
    │             │             │
┌───▼───┐    ┌────▼────┐   ┌────▼────┐
│usuario│    │agenda-  │   │histórico│
│service│    │mento    │   │service  │
│ :3000 │    │service  │   │ :3003   │
└───────┘    │ :3001   │   └─────────┘
            └─────────┘
```

## 🛣️ Rotas Configuradas

### **Endpoints Públicos (sem autenticação)**
- **POST** `/api/usuario/login` → **orchestrator-service**

### **Endpoints Protegidos (com autenticação JWT)**
  
- **PUT/PATCH** `/api/agendamento/edicao/**` → **orchestrator-service**
  - **Perfis:** ADMIN, MEDICO, ENFERMEIRO
  
- **POST** `/api/agendamento/criacao` → **orchestrator-service**
  - **Perfis:** ADMIN, MEDICO, ENFERMEIRO, PACIENTE

## 🔐 Controle de Acesso por Perfil

| Perfil | Login | Criar Usuário | Consultar Usuário | Editar Agendamento | Criar Agendamento |
|--------|-------|---------------|-------------------|-------------------|-------------------|
| **ADMIN** | ✅ | ✅ | ✅ | ✅ | ✅ |
| **MEDICO** | ✅ | ✅ | ✅ | ✅ | ✅ |
| **ENFERMEIRO** | ✅ | ✅ | ✅ | ✅ | ✅ |
| **PACIENTE** | ✅ | ✅ | ✅ | ❌ | ✅ |

## 🔧 Responsabilidades

### **API Gateway (appointment-service)**
- ✅ **Autenticação**: Validação de tokens JWT
- ✅ **Autorização**: Controle de acesso por perfil
- ✅ **Rate Limiting**: Controle de taxa de requisições
- ✅ **Logging**: Registro de todas as requisições
- ✅ **CORS**: Configuração de Cross-Origin
- ✅ **Headers Injection**: Adiciona `X-User-Profile`, `X-Username`

### **Orchestrator Service**
- ✅ **Roteamento**: Direciona para microserviços corretos
- ✅ **Load Balancing**: Distribui carga entre instâncias
- ✅ **Circuit Breaker**: Proteção contra falhas em cascata
- ✅ **Service Discovery**: Localização dinâmica de serviços
- ✅ **Retry Logic**: Tentativas automáticas em caso de falha

### **Microserviços (usuario-service, agendamento-service, etc.)**
- ✅ **Lógica de Negócio**: Processamento específico do domínio
- ✅ **Persistência**: Gerenciamento de dados
- ✅ **Validações**: Regras de negócio específicas
- ✅ **Eventos**: Publicação em Kafka

## 🌊 Fluxo de uma Requisição

1. **Cliente** → `POST /api/agendamento/criacao`
2. **API Gateway** → Valida token JWT
3. **API Gateway** → Verifica se perfil tem permissão
4. **API Gateway** → Adiciona headers `X-User-Profile`, `X-Username`
5. **API Gateway** → Encaminha para `orchestrator-service:8080`
6. **Orchestrator** → Identifica que deve ir para `agendamento-service`
7. **Orchestrator** → Aplica load balancing se múltiplas instâncias
8. **Orchestrator** → Encaminha para `agendamento-service:3001`
9. **Agendamento Service** → Processa a criação do agendamento
10. **Resposta** → Volta pelo mesmo caminho até o cliente

## 🔄 Benefícios da Arquitetura

### **Para o API Gateway:**
- **Foco único**: Apenas segurança e roteamento básico
- **Performance**: Não precisa conhecer todos os microserviços
- **Manutenibilidade**: Mudanças de roteamento ficam no orchestrator
- **Escalabilidade**: Pode escalar independentemente

### **Para o Orchestrator:**
- **Flexibilidade**: Pode mudar roteamento sem afetar o gateway
- **Inteligência**: Pode aplicar lógicas complexas de roteamento
- **Resiliência**: Circuit breakers e retry específicos por serviço
- **Observabilidade**: Métricas detalhadas de cada microserviço

### **Para os Microserviços:**
- **Isolamento**: Protegidos atrás do orchestrator
- **Simplicidade**: Não precisam se preocupar com autenticação
- **Foco**: Apenas lógica de negócio
- **Autonomia**: Podem evoluir independentemente

## 📊 Monitoramento e Observabilidade

- **API Gateway**: Logs de autenticação, autorização e performance
- **Orchestrator**: Métricas de roteamento, latência e disponibilidade
- **Microserviços**: Logs de negócio e métricas específicas do domínio

Esta arquitetura garante **separação clara de responsabilidades**, **alta disponibilidade** e **facilidade de manutenção** do sistema hospitalar.