package com.albertson.spark_poc.core.utils

sealed class Results<out T> {
       object Loading: Results<Nothing>()
       data class Success<out T>(val data: T) : Results<T>()
       data class Error(val errorMessage: String) : Results<Nothing>()
}