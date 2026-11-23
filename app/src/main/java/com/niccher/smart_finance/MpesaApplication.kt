package com.niccher.smart_finance

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.niccher.smart_finance.helpers.SyncScheduler

class MpesaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply Material You dynamic colors to all activities
        DynamicColors.applyToActivitiesIfAvailable(this)

        SyncScheduler.updateSyncSchedule(this)
    }
}
