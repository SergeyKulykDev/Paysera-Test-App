package com.serhiikulyk.payseratestapp

import com.serhiikulyk.payseratestapp.use_cases.convertCurrency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

val conversionRates = mapOf(
    "EUR" to 1.0,    // Base rate, 1 EUR = 1 EUR
    "USD" to 1.12,  // Example: 1 EUR = 1.12 USD
    "UAH" to 39.5  // Example: 1 EUR = 39.5 UAH
)


class ConvertCurrencyUnitTest {

    @Test
    fun testUsdToUah() {
        val amountInUSD = 100.0
        val expected = 3526.785714285714 // 100 USD -> EUR -> UAH
        val actual = convertCurrency(amountInUSD, "USD", "UAH", rates = conversionRates)
        assertEquals(expected, actual, 0.000001)
    }

    @Test
    fun testEurToUsd() {
        val amountInEUR = 100.0
        val expected = 112.0 // 100 EUR -> USD
        val actual = convertCurrency(amountInEUR, "EUR", "USD", rates = conversionRates)
        assertEquals(expected, actual, 0.000001)
    }

    @Test
    fun testUahToUsd() {
        val amountInUAH = 100.0
        val expected = 2.8366 // 100 UAH -> EUR -> USD
        val actual = convertCurrency(amountInUAH, "UAH", "USD", rates = conversionRates)
        assertEquals(expected, actual, 0.0001)
    }

    @Test
    fun testUnknownFromCurrency() {
        assertThrows(IllegalArgumentException::class.java) {
            convertCurrency(100.0, "XYZ", "USD", rates = conversionRates)
        }
    }

    @Test
    fun testUnknownToCurrency() {
        assertThrows(IllegalArgumentException::class.java) {
            convertCurrency(100.0, "USD", "XYZ", rates = conversionRates)
        }
    }
}

inline fun processData(crossinline action: () -> Unit) {
    // Perform some pre-processing
    println("Pre-processing before action")

    // Execute the action asynchronously
    Thread {
        // Perform the action
        action()
    }.start()
}

fun main() {
    println("Starting main function")

    // Call processData with a lambda that has a return statement
    processData {
        // This return statement would normally exit the main function, but it's not allowed because of crossinline
        return@processData
    }

    println("End of main function")
}

