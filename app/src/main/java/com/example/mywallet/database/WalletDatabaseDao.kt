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
    fun updateAccount(account: Account)
    @Query("SELECT * FROM account_table ORDER BY AccountID DESC")
    fun getAllAccounts(): LiveData<List<Account>>
}