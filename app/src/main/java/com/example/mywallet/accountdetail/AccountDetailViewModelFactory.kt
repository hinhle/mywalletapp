package com.example.mywallet.accountdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mywallet.database.WalletDatabaseDao
import com.example.mywallet.financetracker.FinanceTrackerViewModel

class AccountDetailViewModelFactory (
    private val dataSource: WalletDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountDetailViewModel::class.java)) {
            return AccountDetailViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}