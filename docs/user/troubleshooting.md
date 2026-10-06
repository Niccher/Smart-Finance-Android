# Troubleshooting Guide (Operator) — Smart Financial SMS (Android Client)

Common runtime issues when running the Android client.

---

## 1. App Cannot Reach Backend

**Symptom**: Network timeout or `Failed to connect to /127.0.0.1`.

**Cause**: `localhost` refers to the Android device itself.

**Remedy**:
- If using an emulator, set backend URL to `http://10.0.2.2/` (port 80).
- If using a physical phone, ensure phone and host PC are on the same Wi-Fi network and use your PC's LAN IP address (`http://192.168.x.x/`).
- Use the in-app **Test Connection** button to verify network reachability.

---

## 2. Cleartext HTTP Error

**Symptom**: `CLEARTEXT communication to 10.0.2.2 not permitted by network security policy`.

**Remedy**:
Ensure `android:usesCleartextTraffic="true"` is present in `network_security_config.xml` or `AndroidManifest.xml` for debug development builds.

---

## 3. SMS Not Ingested

**Symptom**: Dashboard displays 0 messages found.

**Remedy**:
1. Check that SMS permission is enabled in device system settings (**Settings $\to$ Apps $\to$ Smart Financial SMS $\to$ Permissions**).
2. Note that the app tracks a timestamp watermark: only SMS received after the last sync watermark will be read on subsequent runs.
3. For initial test setup, trigger a full batch upload from the Settings page.

---

## 4. AI Chat Not Responding

**Symptom**: Chat responses fail with HTTP 403 or 404 error.

**Remedy**:
- Ensure the companion app is updated to **v3.5.0**, which routes queries through the WebApp gateway (`/api/v1/chat` on port 80) rather than attempting direct connections to internal port 8001.
- Verify the active LLM engine is running on the backend by visiting `http://localhost/dashboard/chat`.

---

## 5. Glance Widget Displaying Stale Balance

**Symptom**: Homescreen widget doesn't reflect latest transaction.

**Remedy**:
- Tap the **Sync Now** button embedded directly on the widget.
- Disable aggressive battery optimization for Smart Financial SMS in Android system settings so WorkManager background tasks can fire reliably.
