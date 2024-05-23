package com.serhiikulyk.payseratestapp.utils

suspend fun <T> result(completion: suspend () -> T): Result<T> {
    return try {
        val value = completion.invoke()
        Result.success(value)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}
