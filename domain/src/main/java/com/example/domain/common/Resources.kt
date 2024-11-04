package com.example.domain.common

sealed class Resources<out T>{
    object Loading: Resources<Nothing>()
    data class Success<out T>(val data: T?): Resources<T>()
    data class Error(val message: String?): Resources<Nothing>()

}
