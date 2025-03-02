package com.mfatihceliik.chatapplication.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.message.MessageContentType
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.ui.useCase.HomeFragmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeFragmentUseCase: HomeFragmentUseCase
): ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "onCleared")
    }

    init {
        getConversations()
    }

    private val _searchConversations = MutableStateFlow<List<ConversationResponse>?>(null)
    val searchConversations = _searchConversations.asStateFlow()

    suspend fun insertUsers(users: List<User>) = homeFragmentUseCase.chatApiRepository.insertUsers(users = users)
    suspend fun insertParticipants(participant: List<Participant>) = homeFragmentUseCase.chatApiRepository.insertParticipants(participants = participant)
    suspend fun insertMessageContentType(messageContentType: MessageContentType) = homeFragmentUseCase.chatApiRepository.insertMessageContentType(messageContentType = messageContentType)
    private suspend fun addConversations(conversationResponse: ConversationResponse) = homeFragmentUseCase.chatApiRepository.addConversation(conversationResponse = conversationResponse)


    fun onConversations() = viewModelScope.launch(Dispatchers.IO) {
        homeFragmentUseCase.onConversationsWithMessages().collect {
            it?.forEach { conversationResponse ->
                addConversations(conversationResponse = conversationResponse)
            }
        }
    }

    fun getConversationsFromDb() = flow {
        homeFragmentUseCase.onConversationsWithMessagesFromDb().collect {
            emit(it)
        }
    }


    val combineLocalAndSocketConversation = combine(
        homeFragmentUseCase.onConversationsWithMessagesFromDb(),
        homeFragmentUseCase.onConversationsWithMessages()
    )  { local, socket ->
        socket?.let {
            /**/
        }
        local
    }

    private val _conversations = MutableSharedFlow<List<ConversationResponse>?>()
    val conversation = _conversations.asSharedFlow()

    private fun getConversations() {
        viewModelScope.launch(Dispatchers.IO) {
            homeFragmentUseCase.onConversationsWithMessages().collect { conversations ->
                _conversations.emit(conversations)
            }
        }
    }

    /*fun searchConversations(searchText: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " searchConversations")
        homeFragmentUseCase.searchConversations(query = searchText)
    }*/
}
