package com.example.mywallet.financetracker

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mywallet.database.Account
import java.text.DecimalFormat
import java.text.NumberFormat

@BindingAdapter("setCurrencyFormat")
fun TextView.setCurrencyFormat(item : Account)
{
    item?.let {
        val formatter: NumberFormat = DecimalFormat("#,###")
        text = formatter.format(item.AccountBalance) + " đ"
    }
}