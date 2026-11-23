package com.niccher.smart_finance.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.niccher.smart_finance.MainActivity
import com.niccher.smart_finance.database.AppDatabase
import com.niccher.smart_finance.helpers.AppPrefs
import com.niccher.smart_finance.helpers.MpesaParser
import com.niccher.smart_finance.services.UploadService
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

/**
 * Modern Jetpack Glance Home Screen Widget built with declarative Jetpack Compose syntax.
 * Replaces legacy RemoteViews with reactive, type-safe compose layouts.
 */
class SmartFinanceGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val (balanceText, spendText, badgeText) = loadWidgetData(context)

        provideContent {
            GlanceTheme {
                GlanceWidgetContent(
                    balance = balanceText,
                    spendInfo = spendText,
                    badge = badgeText
                )
            }
        }
    }

    @Composable
    private fun GlanceWidgetContent(balance: String, spendInfo: String, badge: String) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "Smart Finance",
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = balance,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = spendInfo,
                style = TextStyle(fontSize = 12.sp)
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Button(
                    text = "Open App",
                    onClick = actionStartActivity<MainActivity>()
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Button(
                    text = "Sync",
                    onClick = actionRunCallback<SyncWidgetActionCallback>()
                )
            }
        }
    }

    private suspend fun loadWidgetData(context: Context): Triple<String, String, String> {
        val fmt = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
        val db = AppDatabase.getDatabase(context)
        val recentList = try {
            db.transactionDao().getRecentTransactions(30)
        } catch (e: Exception) {
            emptyList()
        }

        var latestBalance: Float? = null
        for (tx in recentList) {
            val parsed = MpesaParser.parseMessage(tx.body)
            if (parsed?.newBalance != null && parsed.newBalance > 0f) {
                latestBalance = parsed.newBalance
                break
            }
        }

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = cal.timeInMillis

        cal.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonth = cal.timeInMillis

        var todaySpend = 0f
        var monthSpend = 0f
        for (tx in recentList) {
            if (tx.direction == "outgoing") {
                if (tx.timestamp >= startOfDay) todaySpend += tx.amount
                if (tx.timestamp >= startOfMonth) monthSpend += tx.amount
            }
        }

        val safeToday = AppPrefs.getSafeToSpendToday(context, monthSpend)
        val balanceStr = if (latestBalance != null) "KES ${fmt.format(latestBalance)}" else "KES --"
        val spendStr = "Today: KES ${fmt.format(todaySpend)}"
        val badgeStr = if (todaySpend > safeToday && safeToday > 0f) "Over Budget" else "On Track"

        return Triple(balanceStr, spendStr, badgeStr)
    }
}

class SyncWidgetActionCallback : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val uploadIntent = Intent(context, UploadService::class.java)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(uploadIntent)
            } else {
                context.startService(uploadIntent)
            }
        } catch (e: Exception) {
            android.util.Log.e("SmartFinanceGlanceWidget", "Failed to start UploadService from Glance widget", e)
        }
    }
}

class SmartFinanceGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SmartFinanceGlanceWidget()
}
