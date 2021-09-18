package com.asmat.rolando.nightwing.model


// https://developer.android.com/jetpack/guide
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    fun<S> transform(closure: (T) -> S): Resource<S> {
        return when (this) {
            is Success -> {
                Success(closure(this.data!!))
            }
            is Loading -> {
                Loading(this.data?.let(closure))
            }
            is Error -> {
                Error(this.message!!, this.data?.let(closure))
            }
        }
    }
}