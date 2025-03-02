package com.mfatihceliik.chatapplication.ui.useCase

import android.util.Log
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val socketRepository: ChatApiSocketRepository,
) {
    companion object {
        const val TAG = "SendMessageUseCase"
    }
    suspend fun sendTextMessage(textMessageRequest: TextMessageRequest) = flow {
        Log.v(TAG, Thread.currentThread().name + " sendTextMessage")
        socketRepository.sendTextMessage(textMessageRequest = textMessageRequest).collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, " LOADING sendTextMessage")
                Resource.Status.SUCCESS -> it.data?.let { message ->
                    emit(message)
                }
                Resource.Status.ERROR -> {
                    Log.v(TAG, " ERROR sendTextMessage")
                    Log.v(TAG, "${it.message}")
                }
            }
        }
    }

    suspend fun sendImageMessage(imageMessageRequest: ImageMessageRequest) = flow {
        Log.v(TAG, Thread.currentThread().name + " sendTextMessage")
        socketRepository.sendImageMessage(imageMessageRequest = imageMessageRequest).collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, " LOADING sendImageMessage")
                Resource.Status.SUCCESS -> it.data?.let { message ->
                    emit(message)
                }
                Resource.Status.ERROR -> {
                    Log.v(TAG, " ERROR sendImageMessage")
                    Log.v(TAG, "${it.message}")
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}