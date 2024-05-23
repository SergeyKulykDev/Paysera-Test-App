package com.serhiikulyk.payseratestapp.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.serhiikulyk.payseratestapp.use_cases.ConvertCurrencyUseCase.Companion.DEFAULT_FREE_CONVERSIONS
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Prefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.defaultSharedPreferences: SharedPreferences
        get() = this.getSharedPreferences("${this.packageName}_preferences", Context.MODE_PRIVATE)

    var freeConversions: Int
        set(value) {
            context.defaultSharedPreferences.edit { putInt("free_conversions", value) }
        }
        get() {
            return context.defaultSharedPreferences.getInt("free_conversions", DEFAULT_FREE_CONVERSIONS)
        }


}