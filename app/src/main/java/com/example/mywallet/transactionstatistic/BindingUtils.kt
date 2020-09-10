package com.example.mywallet.transactionstatistic

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mywallet.financetracker.TransactionListView
import java.text.DecimalFormat
import java.text.NumberFormat

@BindingAdapter("setCurrencyFormat")
fun TextView.setCurrencyFormat(item : Revenue)
{
    item.let {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val total = Math.abs(item.total)
        val moneyInString = formatter.format(total)
        text = (if(item.total > 0) "+" else "-") + moneyInString + " đ"

    }
}
