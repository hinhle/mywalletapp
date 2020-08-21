package com.example.mywallet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_table")
data class Account (
    @PrimaryKey(autoGenerate = true)
    var AccountID : Long = 0L,

    @ColumnInfo(name = "account_name")
    var AccountName : String = "Tiền mặt",

    @ColumnInfo(name = "account_balance")
    var AccountBalance : Long= 0L,

    @ColumnInfo(name = "account_type")
    var AccountType : AccountType = com.example.mywallet.database.AccountType.Normal
)
