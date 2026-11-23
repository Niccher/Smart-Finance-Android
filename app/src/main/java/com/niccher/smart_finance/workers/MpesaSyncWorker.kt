package com.niccher.smart_finance.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.niccher.smart_finance.helpers.AppPrefs
import com.niccher.smart_finance.services.UploadService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Calendar

class MpesaSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val mode = AppPrefs.getSyncMode(applicationContext)
        if (mode == "nightly") {
            // Nightly Sync Window: Allowed from 8:00 PM (20:00) until 6:00 AM (06:00).
            // Daytime executions (06:00 to 19:59) return failure to wait for the night schedule.
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            if (currentHour in 6..19) {
                return@withContext Result.failure()
            }
        }

        // Register the receiver before starting the service so no completion broadcast is missed.
        val completion = CompletableDeferred<Boolean>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val success = intent?.getBooleanExtra(UploadService.EXTRA_SUCCESS, false) ?: false
                if (!completion.isCompleted) completion.complete(success)
            }
        }
        val filter = IntentFilter(UploadService.ACTION_UPLOAD_COMPLETE)
        ContextCompat.registerReceiver(
            applicationContext,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        val serviceIntent = Intent(applicationContext, UploadService::class.java)
        try {
            ContextCompat.startForegroundService(applicationContext, serviceIntent)
        } catch (e: Exception) {
            try {
                applicationContext.unregisterReceiver(receiver)
            } catch (_: Exception) {
            }
            return@withContext Result.retry()
        }

        // Wait up to ~9 minutes for the foreground service to report completion.
        // On timeout the service keeps running in the foreground and the next
        // scheduled sync will pick up any remaining messages.
        val outcome = withTimeoutOrNull(UPLOAD_AWAIT_MS) { completion.await() }

        try {
            applicationContext.unregisterReceiver(receiver)
        } catch (_: Exception) {
        }

        when (outcome) {
            null -> Result.success()
            true -> Result.success()
            false -> Result.retry()
        }
    }

    companion object {
        // WorkManager's default execution limit is 10 minutes; keep the wait just under it.
        private const val UPLOAD_AWAIT_MS = 540_000L
    }
}
