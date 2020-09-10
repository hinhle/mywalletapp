package com.example.mywallet.transactionstatistic


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mywallet.database.WalletDatabaseDao
import com.example.mywallet.financetracker.TransactionListView

import kotlinx.coroutines.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*


class TransactionStatisticViewModel( dataSource: WalletDatabaseDao

) : ViewModel()  {

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    val database = dataSource

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val revenueList = MutableLiveData<MutableList<Revenue>>()


    @RequiresApi(Build.VERSION_CODES.O)
    val milestone = OffsetDateTime.now().minusDays(30)
    @RequiresApi(Build.VERSION_CODES.O)
    val transactions = database.getAllTransactionsByTime(milestone.format(formatter))




//    fun onGetList(time: String) : List<TransactionListView> {
//        var list : List<TransactionListView>? = null
//        uiScope.launch {
//            list = getTransactionList(time)
//        }
//            Log.i("inside suspend fun", list.toString() )
//        return list!!
//
//    }

//    suspend fun getTransactionList(time : String) : List<TransactionListView>{
//        return withContext(Dispatchers.IO) {
//            val list = database.getAllTransactionsByTime(time)
//            Log.i("inside suspend fun", list?.get(0)?.AccountName)
//            list
//
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSevenDaysStatistic(transactionList :List<TransactionListView>){
        //val milestone = OffsetDateTime.now().minusDays(7)
        //val transactionList = onGetList(milestone.format(formatter))
       // val transactionList = onGetList(milestone.format(formatter))
        Log.i("inside on Seven Days", transactionList.toString() )
        revenueList.value = mutableListOf()
        Log.i("statistic", "outside let")
        transactionList?.let {
            Log.i("statistic", "inside let")
            var total = 0L
            val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
            Log.i("transaction item", transactionList.get(0).toString())
            val revenue = Revenue(timeMark = fmt.format(transactionList?.get(0)?.DateCreated))
            Log.i("revenue item", revenue.toString())
            revenue.revenueID = transactionList?.get(0)?.TransID
            for (item in transactionList!!){
                val tempTime = fmt.format(item.DateCreated)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun onThirtyDayStatistic(transactionList :List<TransactionListView>){
        Log.i("inside on Seven Days", transactionList.toString() )
        revenueList.value = mutableListOf()
        Log.i("statistic", "outside let")
        transactionList?.let {
            Log.i("statistic", "inside let")
            var total = 0L
//            val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
//            Log.i("transaction item", transactionList.get(0).toString())
//            val revenue = Revenue(timeMark = fmt.format(transactionList?.get(0)?.DateCreated))
            val calendar = Calendar.getInstance(Locale.getDefault())
            val date = transactionList?.get(0)?.DateCreated
            calendar.set(date.year, date.monthValue - 1, date.dayOfMonth)
            //calendar.time = Date(date.year, date.monthValue, date.dayOfMonth)
            val revenue = Revenue(timeMark = "Tuần " + calendar.get(Calendar.WEEK_OF_YEAR))
            Log.i("revenue item", revenue.toString())
            revenue.revenueID = transactionList?.get(0)?.TransID
            for (item in transactionList!!){
                calendar.set(item.DateCreated.year, item.DateCreated.monthValue - 1, item.DateCreated.dayOfMonth)
                val tempTime = "Tuần " + calendar.get(Calendar.WEEK_OF_YEAR)
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