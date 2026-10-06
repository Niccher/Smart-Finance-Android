# ADR 0003: Hybrid CameraX QR and 6-Digit Numeric Pairing

- **Status**: Accepted
- **Date**: 2026-09-25
- **Context**: Mobile Client-Server Handshake & Device Registration

---

## Context and Problem Statement

Connecting the Android companion client to a self-hosted Smart Finance Platform backend requires configuring:
- Server Base URL (`http://<IP>:<PORT>/api/v1/`)
- User Authorization Token
- Device Fingerprint Binding

Typing long server URLs and API tokens manually on a mobile keyboard is prone to typos. Conversely, relying strictly on camera QR scanning fails on devices with broken cameras, low ambient lighting, or restricted camera permissions.

---

## Decision

Implement a **hybrid pairing strategy**:

1. **Primary: CameraX Vertical QR Scanner**:
   - The WebApp dashboard renders a setup QR code encoding a JSON payload with `server_url`, `user_id`, and a short-lived `pairing_token`.
   - The mobile app uses CameraX with a vertically locked viewfinder to automatically scan and parse the QR code in under 100ms.
2. **Secondary: 6-Digit Numeric Fallback Code**:
   - The WebApp displays an ephemeral 6-digit PIN alongside the QR code.
   - If the user prefers not to grant camera access or if lighting is insufficient, they enter the 6-digit code in the app.
   - The app resolves the code against the server pairing registry (`POST /api/v1/auth/pair-code`) to obtain credentials.

---

## Consequences

### Positive
- **Frictionless Onboarding**: Scanning the QR code configures the app in a single tap without manual typing.
- **High Accessibility & Resilience**: Users without working cameras or in dark environments can still easily connect using the 6-digit numeric PIN.
- **Security Scoping**: Pairing tokens are single-use and expire after 5 minutes.

### Negative
- Requires maintaining both CameraX barcode analysis pipeline and server-side pairing token cache in Redis.
