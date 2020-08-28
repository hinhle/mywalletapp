package com.example.mywallet.transactionform

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.Account
import com.example.mywallet.database.Category
import com.example.mywallet.database.Transaction
import com.example.mywallet.database.WalletDatabaseDao
import kotlinx.coroutines.*


class TransactionFormViewModel(dataSource: WalletDatabaseDao,
                               application: Application
) : ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var account : Account

    var isIncome : Boolean = true


    fun onSave(accountID : Long, category : Category){
        uiScope.launch {
            val transaction = Transaction(AccountID = accountID, TransCategory = category)
            insert(transaction)
        }

    }

    fun onChangeBalance(money : Long, key: Long){
        uiScope.launch {
            account = getAccount(key)!!
            Log.i("accountID",key.toString())
            Log.i("accountBalance",account.AccountBalance.toString())
            if (isIncome == true) account.AccountBalance += money
                else account.AccountBalance -= money
            withContext(Dispatchers.IO){
                database.updateAccount(account)
            }
        }
    }

    private suspend fun insert(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            database.insertTransaction(transaction)
        }
    }

    private suspend fun getAccount(key: Long): Account? {
        return withContext(Dispatchers.IO) {
            var account = database.getAccount(key)
            account
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}