# Android Service Handbook — Smart Financial SMS

Technical guide for Android engineers covering architecture patterns, CameraX QR scanning, Jetpack Glance app widgets, real-time AI conversational finance, and background sync workers.

---

## 1. Technical Stack & Build Properties

- **Language & Toolchain**: Kotlin 2.0, Android Gradle Plugin 8.7+, Java 17
- **SDK Targets**: `compileSdk = 35`, `targetSdk = 35`, `minSdk = 29` (Android 10.0+)
- **Architecture**: MVVM with Repository Pattern (ViewModel, LiveData, ViewBinding, DataBinding, Jetpack Compose)
- **Serialization & Networking**: Retrofit 2, Moshi, OkHttp 4 with dynamic IV AES interceptor
- **Local Persistence**: Room Database (compiled via KSP) & Jetpack DataStore / Shared Preferences
- **App Widgets**: Jetpack Glance (`MpesaGlanceWidget`)

---

## 2. Ingestion & Synchronization Pipeline

1. **Watermark SMS Scanning**: Queries `Telephony.Sms.CONTENT_URI` filtering by `date > last_upload_time` stored in persistent preferences.
2. **Local SQLite / Room Staging**: Records are staged in an on-device database prior to transmission to prevent transaction loss during network outages.
3. **AES-128-CBC Dynamic IV Encryption**: Payloads are encrypted client-side using a dynamically generated 16-byte IV (`SecureRandom`) prefixed to the payload stream.
4. **Foreground & Background Workers**:
   - `UploadService`: Foreground service with `dataSync` type for reliable large batch historical imports.
   - `MpesaSyncWorker`: Jetpack WorkManager periodic worker scheduled during opportunistic network and charging windows adhering to Android Doze limits.
5. **OkHttp Disk Cache**: 10 MB dedicated cache at `context.cacheDir/http_cache`.
   - **Online**: `Cache-Control: public, max-age=7200` (2-hour response cache).
   - **Offline**: `only-if-cached, max-stale=604800` (serves up to 7-day stale cache when disconnected).

---

## 3. Conversational AI Assistant (`ChatActivity`)

The app integrates an in-app financial assistant:

- **Gateway Communication**: Requests are sent to the WebApp gateway proxy (`POST /api/v1/chat` on port 80), avoiding direct exposure of internal ML ports.
- **Dynamic Model Discovery**: Calls `GET /api/v1/chat/info` to discover active model metadata (Qwen 2.5, DeepSeek, or Gemini) and renders a live status pill.
- **Starter Chips**: Provides one-tap conversational prompts ("How much did I spend this month?", "Calculate my Fuliza fees", "What is my Safe-to-Spend?").
- **Multi-Turn Context & Markdown**: Renders formatted assistant advice with financial breakdown tables and codeblocks.

---

## 4. Homescreen Glance App Widget (`MpesaGlanceWidget`)

Built with Jetpack Glance, providing interactive home screen telemetry:

- **Glanceable KPI Cards**: Displays current balance, today's spend, and calculated Safe-to-Spend daily allowance.
- **Action Callback**: Embedded "Sync Now" button triggers background WorkManager synchronization directly from the home screen launcher without launching the full application.

---

## 5. Loan Tracker & Safe-to-Spend Intelligence

- **Fuliza & Loan Ledger**: `LoanTrackerHelper.kt` parses overdraft alerts, tracking principal borrowed, accrued daily maintenance fees, repayments, and outstanding balances.
- **Safe-to-Spend Calculation**:
  $$\text{Safe-to-Spend} = \frac{\text{Monthly Budget} - \text{Cumulative Month Outflow}}{\text{Days Remaining in Month}}$$
  Visualized as an interactive ring progress indicator on `HomeFragment`.

---

## 6. Security, Biometrics & Pairing

- **CameraX Vertical QR Scanner**: Viewfinder-locked barcode analysis decoding setup payload URLs and authorization tokens.
- **6-Digit Numeric Pairing Fallback**: Fallback mode for devices without camera hardware or in low-visibility environments.
- **Biometric Authentication with Grace Period**: `BiometricPrompt` integrated with a 30-second activity transition grace window, preventing repeated re-prompt loops when switching between apps.
- **Zero-Trust Client Cryptography**: Encryption keys are compiled via `BuildConfig` and never transmitted across the network in plaintext.\n