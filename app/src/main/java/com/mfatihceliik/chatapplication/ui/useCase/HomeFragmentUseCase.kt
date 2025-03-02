package com.mfatihceliik.chatapplication.ui.useCase

import android.util.Log
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.util.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeFragmentUseCase @Inject constructor(
    val chatApiRepository: ChatApiRepository,
    val socketRepository: ChatApiSocketRepository,
) {

    companion object {
        const val TAG = "HomeFragmentUseCase"
    }

    fun onConversationsWithMessages() = flow {
        Log.v(TAG, Thread.currentThread().name + " onConversations")
        socketRepository.onConversations(chatApiRepository.getUserId()).collect {
            when(it.status) {
                Resource.Status.LOADING -> {
                    Log.v(TAG, "LOADING onConversations")
                    //emit(null)
                }
                Resource.Status.SUCCESS -> {
                    it.data?.let { conversations ->
                        Log.v(TAG, "SUCCESS onConversations")
                        emit(conversations)
                    }
                }
                Resource.Status.ERROR ->  {
                    Log.v(TAG,  "ERROR onConversations")
                    //emit(null)
                }
            }
        }
    }

    fun onConversationsWithMessagesFromDb() = flow {
        Log.v(TAG, Thread.currentThread().name + " getConversationsFromDb")
        chatApiRepository.getConversationsWithMessages().collect { conversationWithMessages ->
            emit(conversationWithMessages)
        }
    }

    /*fun searchConversations(query: String) = flow {
        chatApiRepository.getConversationsByTitle(search = query)
            .distinctUntilChanged()
            .debounce(300L)
            .map { conversations ->
                conversations.filter { conversation ->
                    conversation.conversationName.lowercase().trim().contains(query.lowercase().trim())
                }
            }.flowOn(Dispatchers.Default)
            .collect { conversations ->
                emit(conversations)
            }
    }*/
}