┌──────────────────────────────────────────────────────────────────────────────┐
│                         appointment-service 📅                               │
│               ╔═══════════════════════════════════════════╗                  │
│               ║          MICROSSERViÇO DE ENTRADA         ║                  │
│               ║    • Recebe requisições HTTP              ║                  │
│               ║    • Validação inicial                    ║                  │
│               ║    • Inicia o Saga                        ║                  │
│               ╚═══════════════════════════════════════════╝                  │
└───────────────────────────────┬──────────────────────────────────────────────┘
│
▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                     orchestrator-service 🎛️                                 │
│               ╔═══════════════════════════════════════════╗                  │
│               ║          ORQUESTRADOR DO SAGA             ║                  │
│               ║    • Coordena todos os passos             ║                  │
│               ║    • Gerencia transação distribuída       ║                  │
│               ║    • Executa compensação em caso de erro  ║                  │
│               ╚═══════════════════════════════════════════╝                  │
└───────────────────────────────┬──────────────────────────────────────────────┘
│
┌───────────────────────┼───────────────────────┐
│                       │                       │
▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ notificacao-    │     │ historico-      │     │  usuario-       │
│ service 🔔      │     │ service 📊      │     │  service 👥     │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ • Participante  │     │ • Participante  │     │ • Participante  │
│   do Saga       │     │   do Saga       │     │   do Saga       │
│ • Envia         │     │ • Armazena      │     │ • Valida        │
│   notificações  │     │   histórico     │     │   usuários      │
└─────────────────┘     └─────────────────┘     └─────────────────┘# -tech-challenge-fiap-backend-hospital
