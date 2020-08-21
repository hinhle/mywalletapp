package com.example.mywallet.accountdetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.Account
import com.example.mywallet.database.AccountType
import com.example.mywallet.database.WalletDatabaseDao
import kotlinx.coroutines.*

class AccountDetailViewModel(
    dataSource: WalletDatabaseDao,
    application: Application
):ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var accountName : String = ""
    var accountBalance : Long = 0
    private var _accountType = MutableLiveData<AccountType>()
    val accountType : LiveData<AccountType>
        get() = _accountType

    private var _navigateToFinanceTracker = MutableLiveData<Boolean>()
    val navigateToFinanceTracker : LiveData<Boolean>
        get() = _navigateToFinanceTracker

    init {
        Log.i("AccountDetailViewModel", "GameViewModel created!")
        _accountType.value = AccountType.Normal
        Log.i("AccountType", accountType.value?.name?: "abcd")
    }
    private suspend fun insert(account: Account) {
        withContext(Dispatchers.IO) {
            database.insertAccount(account)
        }
    }

    fun onSave() {
        uiScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newAccount = Account(AccountName = accountName, AccountBalance = accountBalance, AccountType = _accountType.value!!)

            insert(newAccount)
        }
    }

    fun onFinanceTrackerClicked(){
        _navigateToFinanceTracker.value = true
    }
    fun onFinanceTrackerNavigated(){
        _navigateToFinanceTracker.value = false
    }

    fun editAccountType(accountType : String){
        Log.i("hàm edit","hàm chạy")
        _accountType.value = when(accountType){
            "Tiền mặt" -> AccountType.Cash
            "Thẻ tín dụng" -> AccountType.CreditCard
            "Đầu tư" -> AccountType.Investment
            "Bảo hiểm" -> AccountType.Insurance
            "Tài khoản tiết kiệm" -> AccountType.Savings
            else -> AccountType.Normal
        }
        Log.i("hàm edit",_accountType.value?.vieName)
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}