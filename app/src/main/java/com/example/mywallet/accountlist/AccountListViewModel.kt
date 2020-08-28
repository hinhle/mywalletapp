package com.example.mywallet.accountlist

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.WalletDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AccountListViewModel(
    dataSource: WalletDatabaseDao,
    application: Application
) : ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val accounts = database.getAllAccounts()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}