package com.example.mywallet.financetracker

import android.annotation.SuppressLint
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.example.mywallet.database.Account
import com.example.mywallet.database.Transaction
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.format.DateTimeFormatter

@SuppressLint("SetTextI18n")
@BindingAdapter("setCurrencyFormat")
fun TextView.setCurrencyFormat(item : Account)
{
    item.let {
        val formatter: NumberFormat = DecimalFormat("#,###")
        text = formatter.format(item.AccountBalance) + " đ"
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("setDateStringFormat")
fun TextView.setDateStringFormat(item : Transaction){
    val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    text = fmt.format(item.DateCreated)
}
@BindingAdapter("setCurrencyFormat")
fun TextView.setCurrencyFormat(item : Transaction)
{
    item.let {
        val formatter: NumberFormat = DecimalFormat("#,###")
        text = formatter.format(item.TransMoney) + " đ"
    }
}