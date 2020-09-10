package com.example.mywallet.transactionstatistic

import com.example.mywallet.database.Category
import java.time.OffsetDateTime


data class Revenue (
    var revenueID: Long = 0L,
    var timeMark : String,
    var total : Long = 0,
    var quantity: Int = 0
)
