package com.serhiikulyk.payseratestapp.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromHashMap(value: HashMap<String, Double>?): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toHashMap(value: String): HashMap<String, Double>? {
        val gson = Gson()
        val type = object : TypeToken<HashMap<String, Double>>() {}.type
        return gson.fromJson(value, type)
    }

}