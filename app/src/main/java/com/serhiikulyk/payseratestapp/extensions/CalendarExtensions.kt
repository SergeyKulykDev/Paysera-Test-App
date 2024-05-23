package com.serhiikulyk.payseratestapp.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.toFormattedString(pattern: String = "yyyy-MM-dd", locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(pattern, locale)
    return formatter.format(this.time)
}

val now: Calendar get() = Calendar.getInstance()