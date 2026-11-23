package com.niccher.smart_finance.database

import androidx.room.Entity
import androidx.room.Fts4

/**
 * SQLite Virtual Table for Full-Text Search (FTS4) across financial SMS transactions.
 * Enables instant offline search queries (e.g. prefix, phrase, and keyword matching)
 * backed by SQLite's match indexing.
 */
@Entity(tableName = "transactions_fts")
@Fts4(contentEntity = TransactionEntity::class)
data class TransactionFts(
    val sender: String,
    val body: String,
    val category: String
)
