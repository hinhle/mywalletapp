package com.example.mywallet.transactionform

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
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

    var isIncome : Boolean = true

    fun onSave(accountID : Long, category : Category, money: Long){
        uiScope.launch {
            val transaction = Transaction(AccountID = accountID, TransCategory = category, TransMoney = money, isIncome = isIncome)
            insert(transaction)
        }

    }

    fun onChangeBalance(key: Long, money : Long) {
        uiScope.launch {
            updateAccount(key, money)
        }

    }

    private suspend fun insert(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            database.insertTransaction(transaction)
            Log.i("insertTransaction", "success")
        }
    }

    private suspend fun updateAccount(key: Long, money : Long){
        withContext(Dispatchers.IO) {
            var account = database.getAccount(key)
            if (isIncome == true) account.AccountBalance += money
            else account.AccountBalance -= money
            database.updateAccount(account)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}