package com.mfatihceliik.chatapplication.util

data class Resource <out T>(val data: T?, val message: String?, val status: Status ) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(data,null, Status.LOADING)
        }

        fun <T> success(data: T, message: String?): Resource<T> {
            return Resource(data, message, Status.SUCCESS)
        }

        fun <T> error(data: T? = null, message: String?): Resource<T> {
            return Resource(data, message, Status.ERROR)
        }
    }
}