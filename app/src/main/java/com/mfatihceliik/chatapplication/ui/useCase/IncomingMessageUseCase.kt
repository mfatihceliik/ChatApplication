package com.mfatihceliik.chatapplication.ui.useCase

import android.util.Log
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IncomingMessageUseCase @Inject constructor(
    private val socketRepository: ChatApiSocketRepository,
) {
    companion object {
        const val TAG = "IncomingMessageUseCase"
    }

    suspend fun onTextMessage() = flow {
        socketRepository.onTextMessage().collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, " LOADING onTextMessage")
                Resource.Status.SUCCESS -> {
                    Log.v(TAG, it.message.toString())
                    it.data?.let { textMessage ->
                        Log.v(TAG, textMessage.toString())
                        emit(textMessage)
                    }
                }
                Resource.Status.ERROR -> {
                    Log.v(TAG, " ERROR onTextMessage")
                    Log.v(TAG, it.message.toString())
                }
            }
        }
    }

    suspend fun onImageMessage() = flow<Message> {
        socketRepository.onImageMessage().flowOn(Dispatchers.IO).collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, " LOADING onImageMessage")
                Resource.Status.SUCCESS -> it.data?.let { imageMessage ->
                    emit(imageMessage)
                }
                Resource.Status.ERROR -> Log.v(TAG, " ERROR onImageMessage")
            }
        }
    }.flowOn(Dispatchers.IO)
}