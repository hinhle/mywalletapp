package com.example.mywallet.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME


    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
    @TypeConverter
    @JvmStatic
    fun toCategory(value: String) : Category {
        return value.let {
            return Category.valueOf(value)
        }
    }
    @TypeConverter
    @JvmStatic
    fun fromCategory(category: Category): String {
        return category.vieName
    }
    @TypeConverter
    @JvmStatic
    fun toPayment(value: String) : Payment {
        return value.let {
            return Payment.valueOf(value)
        }
    }
    @TypeConverter
    @JvmStatic
    fun fromPayment(payment: Payment): String {
        return payment.vieName
    }
    @TypeConverter
    @JvmStatic
    fun toAccountType(value: String) : AccountType {
        return value.let {
            return AccountType.valueOf(value)
        }
    }
    @TypeConverter
    @JvmStatic
    fun fromAccountType(accountType: AccountType): String {
        return accountType.name
    }
}