package com.example.mywallet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var TransID: Long = 0L,
    @ForeignKey(
        entity = Account::class,
        parentColumns = ["AccountID"],
        childColumns = ["AccountID"]
    )
    var AccountID: Long = 0L,
    @ColumnInfo(name = "transaction_money")
    var TransMoney: Long = 0L,
    @ColumnInfo(name = "date_created")
    var DateCreated: OffsetDateTime = OffsetDateTime.now(),
    @ColumnInfo(name = "transaction_category")
    var TransCategory: Category = Category.Other,
    @ColumnInfo(name = "transaction_payment")
    var Payment: Payment = com.example.mywallet.database.Payment.Cash,
    @ColumnInfo(name = "transaction_payee")
    var Payee: String = "",
    @ColumnInfo(name = "transaction_is_income")
    var isIncome: Boolean = true
)