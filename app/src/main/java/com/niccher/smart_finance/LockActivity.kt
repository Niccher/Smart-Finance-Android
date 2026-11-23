package com.niccher.smart_finance

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.niccher.smart_finance.helpers.AppPrefs
import com.niccher.smart_finance.helpers.BiometricHelper

class LockActivity : AppCompatActivity() {

    private lateinit var biometricHelper: BiometricHelper
    private var hasPrompted = false

    companion object {
        var isUnlocked: Boolean = false
        var lastUnlockTime: Long = 0L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )

        biometricHelper = BiometricHelper(this)

        onBackPressedDispatcher.addCallback(this) {}

        val btnUnlock = findViewById<Button>(R.id.btn_unlock)
        val pinLayout = findViewById<LinearLayout>(R.id.pin_layout)
        val etPin = findViewById<EditText>(R.id.et_pin)
        val btnPinUnlock = findViewById<Button>(R.id.btn_pin_unlock)
        val tvSwitch = findViewById<TextView>(R.id.tv_switch_auth)

        val hasPin = AppPrefs.isPinEnabled(this) && AppPrefs.getPinCode(this).isNotEmpty()
        val hasBio = AppPrefs.isBiometricEnabled(this)

        // If neither is enabled, skip lock entirely
        if (!hasPin && !hasBio) {
            isUnlocked = true
            lastUnlockTime = System.currentTimeMillis()
            finish()
            return
        }

        if (hasPin) pinLayout.visibility = android.view.View.VISIBLE

        if (hasBio) {
            btnUnlock.visibility = android.view.View.VISIBLE
            tvSwitch.text = if (hasPin) "Use PIN instead" else ""
        }

        btnUnlock.setOnClickListener {
            showBiometricPrompt()
        }

        var failedAttempts = 0
        var lockoutUntil = 0L

        btnPinUnlock.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now < lockoutUntil) {
                val remainingSec = ((lockoutUntil - now) / 1000).coerceAtLeast(1)
                Toast.makeText(this, "Too many failed attempts. Try again in ${remainingSec}s", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val entered = etPin.text.toString()
            if (AppPrefs.verifyPin(this, entered)) {
                failedAttempts = 0
                isUnlocked = true
                lastUnlockTime = System.currentTimeMillis()
                finish()
            } else {
                failedAttempts++
                if (failedAttempts >= 5) {
                    lockoutUntil = now + 30_000L
                    Toast.makeText(this, "5 failed attempts. Locked for 30 seconds.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Wrong PIN (${5 - failedAttempts} attempts remaining)", Toast.LENGTH_SHORT).show()
                }
                etPin.text.clear()
            }
        }

        tvSwitch.setOnClickListener {
            if (pinLayout.visibility == android.view.View.VISIBLE) {
                pinLayout.visibility = android.view.View.GONE
                btnUnlock.visibility = android.view.View.VISIBLE
                tvSwitch.text = if (hasPin) "Use PIN instead" else ""
            } else {
                pinLayout.visibility = android.view.View.VISIBLE
                btnUnlock.visibility = android.view.View.GONE
                tvSwitch.text = if (hasBio) "Use biometric instead" else ""
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isUnlocked && !hasPrompted && !biometricHelper.isShowing()) {
            val hasBio = AppPrefs.isBiometricEnabled(this)
            if (hasBio) {
                hasPrompted = true
                showBiometricPrompt()
            }
        }
    }

    private fun showBiometricPrompt() {
        biometricHelper.showBiometricPrompt(
            onSuccess = {
                isUnlocked = true
                lastUnlockTime = System.currentTimeMillis()
                finish()
            },
            onError = { error ->
                if (!error.contains("cancel", true) && !error.contains("user cancel", true)) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            },
            onFail = { }
        )
    }
}
