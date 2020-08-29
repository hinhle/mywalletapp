package com.example.mywallet.financetracker

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.WalletDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class FinanceTrackerViewModel(
    dataSource: WalletDatabaseDao,
    application: Application
) : ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val accounts = database.getAllAccounts()
    val transactions = database.getAllTransactions()

    private val _navigateToAccountDetail = MutableLiveData<Boolean>()
    val navigateToAccountDetail: LiveData<Boolean>
        get() = _navigateToAccountDetail

    fun onAccountDetailClicked() {
        _navigateToAccountDetail.value = true
    }

    fun onAccountDetailNavigated() {
        _navigateToAccountDetail.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}