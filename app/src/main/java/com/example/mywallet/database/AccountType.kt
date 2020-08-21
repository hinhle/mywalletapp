package com.example.mywallet.database

enum class AccountType (val vieName : String){
    Normal("Thường"),
    Cash("Tiền mặt"),
    CreditCard("Thẻ tín dụng"),
    Savings("Tài khoản tiết kiệm"),
    Insurance("Bảo hiểm"),
    Investment("Đầu tư")
}