package com.example.mywallet.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mywallet.financetracker.TransactionListView

@Dao
interface WalletDatabaseDao {
    @Insert
    fun insertAccount(account: Account)

    @Update
    fun updateAccount(account: Account) : Int

    @Query("UPDATE account_table SET account_balance = :money WHERE AccountID = :key")
    fun updateAccount(key: Long, money : Long)

    @Query("SELECT * FROM account_table ORDER BY AccountID DESC")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT account_table.account_name AS AccountName, TransID, transaction_category AS TransCategory, transaction_is_income AS isIncome, transaction_money AS TransMoney, date_created AS DateCreated FROM transaction_table INNER JOIN account_table ON transaction_table.AccountID = account_table.AccountID ORDER BY TransID DESC LIMIT :limit")
    fun getAllTransactionsLimit(limit : Int): LiveData<List<TransactionListView>>

    @Query("SELECT * FROM transaction_table ORDER BY TransID DESC ")
    fun getAllTransactions(): LiveData<List<Transaction>>


    @Insert
    fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM account_table WHERE AccountID = :key")
    fun getAccount(key : Long) : Account

}