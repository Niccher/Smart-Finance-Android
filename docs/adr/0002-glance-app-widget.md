# ADR 0002: Jetpack Glance Homescreen App Widget Integration

- **Status**: Accepted
- **Date**: 2026-09-24
- **Context**: Android Companion Client Launcher & Telemetry UX

---

## Context and Problem Statement

Users frequently check their M-Pesa balance, today's total spending, and their Safe-to-Spend daily allowance multiple times per day. Opening the full mobile application repeatedly introduces friction and battery drain, especially if biometric authentication is triggered on each launch.

We needed a low-friction surface to surface live financial KPIs and trigger background synchronization directly from the Android launcher screen.

---

## Decision

Implement **Jetpack Glance** (`MpesaGlanceWidget.kt`) as an interactive home screen widget:

1. **Declarative Compose Architecture**: Use Glance's declarative Kotlin Compose runtime rather than legacy RemoteViews XML layouts, simplifying state synchronization with existing app viewmodels.
2. **Glanceable KPIs**: Render three primary data points:
   - Current Account Balance (KES)
   - Today's Cumulative Outflow (KES)
   - Daily Safe-to-Spend Allowance
3. **One-Tap Background Action**: Include a "Sync Now" button that triggers `MpesaSyncWorker` via `WorkManager` in the background without needing to launch or foreground the main `MainActivity`.

---

## Consequences

### Positive
- **Instant Glanceability**: Users view critical financial health metrics in zero taps directly on their home screen.
- **Unified Compose Tooling**: Developers write widget UI using Jetpack Compose principles matching the rest of the application.
- **Battery & Memory Efficiency**: Widgets refresh strictly on broadcast events (new SMS arrival or sync completion) rather than continuous polling.

### Negative
- Requires dependency on `androidx.glance:glance-appwidget` which increases APK footprint slightly (~300 KB).
