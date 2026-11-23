package com.niccher.smart_finance.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val smsId: Long,
    val sender: String,
    val body: String,
    val amount: Float,
    val category: String,
    val direction: String,
    val carrier: String,
    val simSlot: Int,
    val timestamp: Long,
    val syncStatus: Int = 0 // 0 = Unsynced, 1 = Synced
)
