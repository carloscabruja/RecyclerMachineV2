package es.refil.recyclermachinev2.common

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Error(message: String? = null, data: Nothing? = null) : Resource<Nothing>(data, message)
    class Loading<out T>(data: T? = null) : Resource<T>(data)
}
