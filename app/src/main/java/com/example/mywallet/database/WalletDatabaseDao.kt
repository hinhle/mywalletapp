package com.example.mywallet.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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

    @Query("SELECT * FROM transaction_table ORDER BY TransID DESC LIMIT :limit")
    fun getAllTransactionsLimit(limit : Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM transaction_table ORDER BY TransID DESC ")
    fun getAllTransactions(): LiveData<List<Transaction>>


    @Insert
    fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM account_table WHERE AccountID = :key")
    fun getAccount(key : Long) : Account
}