package com.example.mywallet.financetracker

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.example.mywallet.R
import com.example.mywallet.database.Account
import com.example.mywallet.database.Category
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
fun TextView.setDateStringFormat(item : TransactionListView){
    val fmt : DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    text = fmt.format(item.DateCreated)
}
@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("setCurrencyFormat")
fun TextView.setCurrencyFormat(item : TransactionListView)
{
    item.let {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val moneyInString = formatter.format(item.TransMoney)
        text = (if(item.isIncome) "+" else "-") + moneyInString + " đ"

    }
}
@BindingAdapter("setCategoryImage")
fun ImageView.setCategoryImage(item: TransactionListView){
    setImageResource(when (item.TransCategory) {
        Category.House -> R.drawable.ic_baseline_home_24
        Category.FoodAndBeverage -> R.drawable.ic_baseline_fastfood_24
        Category.Vehicle -> R.drawable.ic_baseline_directions_car_24
        Category.Finance -> R.drawable.ic_baseline_attach_money_24
        Category.Entertainment-> R.drawable.ic_baseline_accessibility_new_24
        Category.Shopping -> R.drawable.ic_baseline_shopping_cart_24
        Category.Transportation -> R.drawable.ic_baseline_directions_car_24
        Category.Media -> R.drawable.ic_baseline_emoji_objects_24
        Category.Investment -> R.drawable.ic_baseline_monetization_on_24
        Category.Income -> R.drawable.ic_baseline_emoji_transportation_24

        else -> R.drawable.ic_baseline_add_24
    })
}