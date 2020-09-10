package com.example.mywallet.transactionstatistic

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mywallet.database.WalletDatabaseDao


class TransactionStatisticViewModelFactory (
    private val dataSource: WalletDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionStatisticViewModel::class.java)) {
            return TransactionStatisticViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}