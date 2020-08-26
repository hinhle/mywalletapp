package com.example.mywallet.transactioncategory

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mywallet.R
import com.example.mywallet.database.Category


@BindingAdapter("setCategoryName")
fun TextView.setCategoryName(item : Category){
    text = item.vieName
}
@BindingAdapter("setCategoryImage")
fun ImageView.setCategoryImage(item: Category){
    setImageResource(when (item) {
        Category.House -> R.drawable.ic_baseline_home_24
        Category.FoodAndBeverage -> R.drawable.ic_baseline_fastfood_24
        Category.Vehicle -> R.drawable.ic_baseline_directions_car_24
        Category.Finance -> R.drawable.ic_baseline_attach_money_24
        Category.Entertainment-> R.drawable.ic_baseline_accessibility_new_24

        else -> R.drawable.ic_baseline_add_24
    })
}