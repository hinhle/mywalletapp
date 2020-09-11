package com.example.mywallet.transactionstatistic


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.WalletDatabaseDao
import com.example.mywallet.financetracker.TransactionListView

import kotlinx.coroutines.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TransactionStatisticViewModel( dataSource: WalletDatabaseDao

) : ViewModel()  {

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val revenueList = MutableLiveData<MutableList<Revenue>>()

    lateinit var transactions: LiveData<List<TransactionListView>>

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSevenDayClick() {
    val milestone = OffsetDateTime.now().minusDays(7)
    transactions = database.getAllTransactionsByTime(milestone.format(formatter))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onThirtyDayClick(){
        val milestone = OffsetDateTime.now().minusDays(30)
        transactions = database.getAllTransactionsByTime(milestone.format(formatter))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTwelveWeekClick(){
        val milestone = OffsetDateTime.now().minusWeeks(12)
        transactions = database.getAllTransactionsByTime(milestone.format(formatter))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSixMonthClick(){
        val milestone = OffsetDateTime.now().minusMonths(6)
        transactions = database.getAllTransactionsByTime(milestone.format(formatter))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onOneYearClick(){
        val milestone = OffsetDateTime.now().minusYears(1)
        transactions = database.getAllTransactionsByTime(milestone.format(formatter))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSevenDaysStatistic(transactionList: List<TransactionListView>){

        Log.i("inside on Seven Days", transactionList.toString() )
        val operation : (TransactionListView) -> String = {
            val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
            fmt.format(it.DateCreated)
        }
        createRevenueList(transactionList, operation)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onThirtyDayStatistic(transactionList :List<TransactionListView>){
        val operation : (TransactionListView) -> String = {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.set(it.DateCreated.year, it.DateCreated.monthValue - 1, it.DateCreated.dayOfMonth)
            "Tuần " + calendar.get(Calendar.WEEK_OF_YEAR)
        }
        createRevenueList(transactionList, operation)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTwelveWeekStatistic(transactionList :List<TransactionListView>){
        val operation : (TransactionListView) -> String = {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.set(it.DateCreated.year, it.DateCreated.monthValue - 1, it.DateCreated.dayOfMonth)
            "Tuần " + calendar.get(Calendar.WEEK_OF_YEAR)
        }
        createRevenueList(transactionList, operation)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSixMonthStatistic(transactionList: List<TransactionListView>){

        val operation : (TransactionListView) -> String = {
            val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("MM")
            "Tháng " + fmt.format(it.DateCreated)
        }
        createRevenueList(transactionList, operation)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onOneYearStatistic(transactionList: List<TransactionListView>){

        val operation : (TransactionListView) -> String = {
            val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("MM")
            "Tháng " + fmt.format(it.DateCreated)
        }
        createRevenueList(transactionList, operation)

    }

    fun createRevenueList(transactionList: List<TransactionListView>, operation: (TransactionListView) -> String){
        revenueList.value = mutableListOf()
        Log.i("statistic", "outside let")
        transactionList?.let {
            Log.i("statistic", "inside let")

            val revenue = Revenue(timeMark = operation(transactionList[0]))
            Log.i("revenue item", revenue.toString())
            revenue.revenueID = transactionList?.get(0)?.TransID
            for (item in transactionList!!){
                val tempTime = operation(item)
                if (tempTime != revenue.timeMark){
                    val newRevenue = revenue.copy()
                    revenueList.value!!.add(newRevenue)
                    revenue.timeMark = tempTime
                    revenue.total = 0L
                    revenue.revenueID = item.TransID
                    revenue.quantity = 0
                }
                if (item.isIncome) revenue.total += item.TransMoney
                else revenue.total -= item.TransMoney
                revenue.quantity++
                revenue.revenueID = item.TransID

            }
            revenueList.value!!.add(revenue)

        }
        Log.i("revenue list", revenueList.value.toString())
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}