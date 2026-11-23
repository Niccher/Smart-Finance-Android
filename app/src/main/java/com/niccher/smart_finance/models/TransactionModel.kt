package com.niccher.smart_finance.models

data class TransactionModel(
    val id: String,
    val date: Long,
    val address: String,
    val body: String,
    var type: String = "Unknown",
    var amount: Double = 0.0,
    var reference: String = "",
    var name: String = ""
)
