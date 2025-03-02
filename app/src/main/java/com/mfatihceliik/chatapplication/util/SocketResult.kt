package com.mfatihceliik.chatapplication.util

import com.mfatihceliik.chatapplication.data.entity.base.SocketResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseSocketDataSource {
    companion object {
        private const val TAG = "BaseSocketDataSource"
    }

    protected suspend fun <T> getResult(call: suspend () -> Flow<SocketResponse<T>>): Flow<Resource<T>> = flow {
        try {
            emit(Resource.loading())
            val responseFlow = call.invoke()
            responseFlow.collect { response ->
                val data = response.data
                val message = response.message
                if (response.success)
                    emit(Resource.success(data = data!!, message = message))
                else
                    emit(Resource.error(data = data, message))

            }
        } catch (exception: Exception) {
            emit(error("Error Context is NETWORK: $exception"))
        }
    }.flowOn(Dispatchers.IO)

    private fun <T> error(message: String): Resource<T> {
        return Resource.error(message = message)
    }
    /*
    private fun <T> success(data: T, message: String?): Resource<T> {
        return Resource.success(data = data, message = message)
    }*/
}
