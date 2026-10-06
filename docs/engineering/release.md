# Ecosystem Release & Compatibility Matrix — Android Client

This document tracks versioning and inter-component compatibility for **Smart Financial SMS** (`Smart-Finance-Android`) within the Smart Finance ecosystem.

---

## 1. Version Matrix

| Android Version (`versionName`) | Android Build (`versionCode`) | Target SDK | Master Platform Version | Minimum Database Migration |
|---|---|---|---|---|
| **v3.5.0** | **4** | **35 (Android 15)** | **v3.5.0** | `2026-09-25` (`tbl_Chat_Messages`) |
| **v3.4.0** | **3** | 35 | v3.4.0 | `2026-09-09` |
| **v3.3.0** | **2** | 34 | v3.3.0 | `2026-09-07` |
| **v3.2.0** | **1** | 34 | v3.2.0 | `2026-08-11` |

---

## 2. API Compatibility Policy

- **REST Base URL**: All API calls are namespaced under `/api/v1/`.
- **Dynamic Version Discovery**: On application startup or when opening the About screen, the app calls `GET /api/v1/system/version` to determine if mandatory updates or database schema changes have been deployed on the server.
- **Port 80 Mobile Gateway Proxy**: From v3.5.0 onwards, all AI Chat queries route through the WebApp gateway (`/api/v1/chat`), eliminating dependency on opening internal microservice port 8001 directly to mobile traffic.

---

## 3. Pre-Release Quality Checklist

Prior to publishing a new release:

1. **Gradle Build Verification**:
   - Increment `versionCode` (e.g., `4` $\to$ `5`) and `versionName` in `app/build.gradle.kts`.
   - Ensure `minSdk = 29` and `targetSdk = 35`.
2. **Automated Testing**:
   - Execute all unit tests: `./gradlew testDebugUnitTest`.
   - Verify KSP Room database migrations and Glance App Widget compilers finish cleanly.
3. **Hardware & Emulator Sanity**:
   - Test CameraX vertical barcode scan and 6-digit numeric pairing code fallback.
   - Verify Biometric prompt 30-second grace window prevents unwanted re-prompting.
   - Verify AES-128 dynamic IV encrypted payload decodes properly on the backend.
4. **Documentation Quality**:
   - Run `python scripts/lint-docs.py .` to ensure zero broken links or leaked secrets.
