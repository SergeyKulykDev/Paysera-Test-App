package com.serhiikulyk.payseratestapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serhiikulyk.payseratestapp.ui.BalanceItem
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Entity(tableName = "balances")
data class BalanceEntity(
    @PrimaryKey
    val currency: String,
    var amount: Double = 0.0
)

fun BalanceEntity.toBalanceItem() = BalanceItem(currency = currency, formatted = formatted())

fun Double.toCurrencyFormat(currencyCode: String, locale: Locale = Locale.getDefault()): String {
    val currency = Currency.getInstance(currencyCode)
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale).apply {
        this.currency = currency
        this.minimumFractionDigits = 2
        this.maximumFractionDigits = 2
    }
    return currencyFormatter.format(this)
}


fun BalanceEntity.formatted(): String {
    val currency = Currency.getInstance(currency)
    val currencyFormatter = NumberFormat.getCurrencyInstance().apply {
        this.currency = currency
        this.minimumFractionDigits = 2
        this.maximumFractionDigits = 2
    }
    val formattedNumber = currencyFormatter.format(amount)
        .replace(currency.symbol, "")
        .trim()

    // Append the currency code to the formatted number like design
    return "$formattedNumber $currency"
}

fun Double.formatted(): String {
    val currencyFormatter = NumberFormat.getInstance().apply {
        this.minimumFractionDigits = 2
        this.maximumFractionDigits = if (this@formatted > 1) 2 else 4
    }
    return currencyFormatter
        .format(this)
        .trim()
}

fun Double.formatted(currency: String): String {
    return "${formatted()} $currency"
}