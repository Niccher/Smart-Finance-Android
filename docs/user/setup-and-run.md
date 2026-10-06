# Setup and Run Guide — Smart Financial SMS (Android Client)

This guide walks through installing, configuring, and operating the Android client.

---

## 1. Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17+**
- Android device or emulator running **API 29+** (Android 10.0+)
- Running **Smart Finance Platform** backend on port 80

---

## 2. Step-by-Step Run Instructions

### Step 1: Open in Android Studio
1. Launch Android Studio.
2. Select **Open** and choose the `Smart-Finance-Android` repository directory.
3. Allow Gradle to sync dependencies.

### Step 2: Build and Run on Target Device
1. Connect an Android physical device via USB (with USB Debugging enabled) or start an Android Virtual Device (AVD).
2. Press **Run** (`Shift+F10`) or execute from terminal:
   ```bash
   ./gradlew installDebug
   ```

### Step 3: Configure Backend URL
Upon first launch (or in **Settings**):
- **Android Emulator**: Set URL to `http://10.0.2.2/`
- **Physical Device**: Set URL to your development machine's LAN IP, e.g. `http://<YOUR_LAN_IP>/`
- Tap **Test Connection** to confirm connectivity to `/api/v1/system/version`.

### Step 4: Grant Permissions & Sync
1. Grant SMS Read permission when prompted.
2. Tap **Fetch & Sync** on the Home Dashboard to trigger initial encrypted upload.
3. Add the **M-Pesa Glance Widget** to your home screen for quick daily balance and safe-to-spend tracking.
