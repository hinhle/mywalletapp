package com.example.mywallet.database

enum class Payment(val vieName: String) {
    Cash("Tiền mặt"),
    DebitCard("Thẻ ghi nợ"),
    CreditCard("Thẻ tín dụng"),
    BankTransfer("Chuyển khoản ngân hàng"),
    Coupon("Phiếu quà tặng")
}