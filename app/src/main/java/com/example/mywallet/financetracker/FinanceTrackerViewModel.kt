package com.example.mywallet.financetracker

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.WalletDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class FinanceTrackerViewModel(
    dataSource: WalletDatabaseDao,
    application: Application
) : ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val accounts = database.getAllAccounts()
    val transactions = database.getAllTransactionsLimit(5)
    val accounts2 = database.getAllAccounts2()
//    @RequiresApi(Build.VERSION_CODES.O)
//    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
//    val milestone = OffsetDateTime.now().minusDays(7)
//    @RequiresApi(Build.VERSION_CODES.O)
//    val time = milestone.format(formatter)


    private val _navigateToAccountDetail = MutableLiveData<Boolean>()
    val navigateToAccountDetail: LiveData<Boolean>
        get() = _navigateToAccountDetail

    fun onAccountDetailClicked() {
        Log.i("inside onAccountDetail", accounts.value.toString() )
        Log.i("inside onAccountDetail", accounts2.value.toString() )
        _navigateToAccountDetail.value = true
    }

    fun onClick(){
        Log.i("inside onAccountDetail", accounts.value.toString() )
        Log.i("inside onAccountDetail", accounts2.value.toString() )
    }
    fun onAccountDetailNavigated() {
        _navigateToAccountDetail.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}