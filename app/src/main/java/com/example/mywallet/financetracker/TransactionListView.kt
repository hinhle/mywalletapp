package com.example.mywallet.financetracker

import com.example.mywallet.database.Category

import java.time.OffsetDateTime

data class TransactionListView(
    var TransID: Long = 0L,
    var TransMoney: Long = 0L,
    var DateCreated: OffsetDateTime = OffsetDateTime.now(),
    var TransCategory: Category = Category.Other,
    var isIncome : Boolean = true,
    var AccountName: String = "Tiền mặt"
)