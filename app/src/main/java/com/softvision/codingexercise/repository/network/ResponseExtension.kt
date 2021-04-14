package com.softvision.codingexercise.data

import android.util.Log
import retrofit2.Response

fun <T> Response<T>.checkResponse(): T {
    return if (isSuccessful && body() != null) {
        body() ?: run {
            Log.d("ResponseExtension", "Empty response body")
            throw Throwable("An error occurred")
        }
    } else {
        throw Throwable(errorBody()?.string())
    }
}
