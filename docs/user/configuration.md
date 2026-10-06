# Configuration Guide — Smart Financial SMS (Android Client)

Configuration settings managed inside the Android application.

---

## 1. Application Settings (In-App)

| Setting | Location | Purpose | Default |
|---------|----------|---------|---------|
| **Backend URL** | Setup / Settings | Base URL for REST API calls | `http://10.0.2.2/` |
| **Biometric Lock** | Settings | Enforce fingerprint / face unlock with 30s grace window | Disabled |
| **Monthly Budget** | Settings / Budget | Sets target spending limit for Safe-to-Spend calculations | KES 0 |
| **Dark Theme** | Settings | Switch between Light, Dark, and System theme | System Default |
| **Sync Schedule** | Background | Periodic sync worker (`MpesaSyncWorker`) | Automatic / Opportunistic |
| **Glance Widget** | Home Screen | Real-time balance and safe-to-spend allowance tile | Enabled on placement |

---

## 2. Network Addresses for Local Development

- **Android Studio Emulator**: Must use `http://10.0.2.2/` (port 80). `localhost` refers to the emulator itself and will fail.
- **Physical Phone**: Must use your computer's local Wi-Fi IP address (e.g. `http://192.168.x.x/`). Ensure your computer firewall permits incoming connections on port 80.
- **AI Chat Gateway**: All conversational AI assistant queries automatically route through the WebApp port 80 gateway (`/api/v1/chat`), requiring no separate port configuration.
