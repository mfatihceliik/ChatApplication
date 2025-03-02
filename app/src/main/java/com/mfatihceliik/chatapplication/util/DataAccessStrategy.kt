package com.mfatihceliik.chatapplication.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


fun <T> performNetworkOperation(call: suspend() -> Resource<T>, saveToken: (token: String) -> Unit): LiveData<Resource<T>> {
    return liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val networkCall = call.invoke()
        if(networkCall.status == Resource.Status.SUCCESS) {
            val message = networkCall.message!!
            val data = networkCall.data!!
            if(data is LoginResponse) {
                saveToken(data.token)
            }
            emit(Resource.success(data,message))
        } else if(networkCall.status == Resource.Status.ERROR) {
            val message = networkCall.message!!
            emit(Resource.error(message = message))
        }
    }
}

fun<T> performNetworkOperationByFlow(call: suspend () -> Flow<Resource<T>>): Flow<Resource<T>> = flow {

    emit(Resource.loading())
    val networkCall = call.invoke()
    networkCall.collect { resource: Resource<T> ->
        if(resource.status == Resource.Status.SUCCESS) {
            val message = resource.message
            val data = resource.data!!
            emit(Resource.success(data = data, message =  message))
        } else if(resource.status == Resource.Status.ERROR) {
            val message = resource.message
            val data = resource.data
            emit(Resource.error(data = data, message = message))
        }
    }
}.flowOn(Dispatchers.IO)











