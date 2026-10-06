# Smart Financial SMS (Android Client) — Engineering Documentation

Welcome to the engineering documentation for **Smart Financial SMS** (`Smart-Finance-Android`), the native Android mobile companion client within the Smart Finance ecosystem.

System-wide architecture blueprints, ERD schemas, and cross-repo sequence flows are anchored in the [Smart-Finance-Platform Architecture Hub](https://github.com/Niccher/Smart-Finance-Platform/tree/main/docs/architecture).

---

## Documentation Index

| I want to… | Go here |
|------------|---------|
| **Install and test the debug APK** | [../README.md](../README.md) |
| **Setup & run on emulator / device** | [user/setup-and-run.md](user/setup-and-run.md) |
| **Review user configuration options** | [user/configuration.md](user/configuration.md) |
| **Resolve mobile operational issues** | [user/troubleshooting.md](user/troubleshooting.md) |
| **Understand Android architecture & components** | [services/android.md](services/android.md) |
| **Review Android Studio & Gradle dev environment** | [engineering/local-development.md](engineering/local-development.md) |
| **Inspect AES-128 encryption & biometric security** | [engineering/security.md](engineering/security.md) |
| **Review release matrix & version compatibility** | [engineering/release.md](engineering/release.md) |
| **Add new UI screens, charts, or sync routines** | [engineering/making-changes.md](engineering/making-changes.md) |
| **Run unit & instrumentation tests** | [engineering/testing.md](engineering/testing.md) |
| **Troubleshoot emulator networking or Gradle issues** | [engineering/troubleshooting.md](engineering/troubleshooting.md) |
| **ADR 0001: Dynamic IV AES-128 payload encryption** | [adr/0001-client-side-aes128-dynamic-iv.md](adr/0001-client-side-aes128-dynamic-iv.md) |
| **ADR 0002: Homescreen Glance app widget** | [adr/0002-glance-app-widget.md](adr/0002-glance-app-widget.md) |
| **ADR 0003: Hybrid CameraX QR & numeric pairing** | [adr/0003-hybrid-qr-numeric-pairing.md](adr/0003-hybrid-qr-numeric-pairing.md) |
| **Follow contribution & pull request guidelines** | [engineering/contributing.md](engineering/contributing.md) |

---

## Master Platform Repository

- **Smart Finance Platform (Master Monorepo)**: [Niccher/Smart-Finance-Platform](https://github.com/Niccher/Smart-Finance-Platform) (PHP 8.3 CodeIgniter 4 WebApp, Python 3.12 FastAPI LLM Microservice, Redis 7 In-Memory Cache, MySQL 8.4)
