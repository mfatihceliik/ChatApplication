package com.mfatihceliik.chatapplication.ui.conversation

import android.content.Context
import android.text.Editable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.enums.MessageType
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.message.TextMessage
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.ui.useCase.ChatFragmentUseCase
import com.mfatihceliik.chatapplication.ui.useCase.IncomingMessageUseCase
import com.mfatihceliik.chatapplication.ui.useCase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatApiRepository: ChatApiRepository,
    private val socketRepository: ChatApiSocketRepository,
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val chatFragmentUseCase: ChatFragmentUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val incomingMessageUseCase: IncomingMessageUseCase,
): ViewModel() {

    companion object {
        private const val TAG = "ChatViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "onCleared")
    }

    init {
        onTextMessage()
        onImageMessage()
    }

    val conversationResponse: ConversationResponse = savedStateHandle["conversation"]!!

    private val _onMessage = MutableSharedFlow<MessageUiModel>()
    val onMessage: SharedFlow<MessageUiModel> = _onMessage.asSharedFlow()

    private val _sendMessageButtonVisibility = MutableStateFlow(false)
    val sendMessageButtonVisibility = _sendMessageButtonVisibility.asStateFlow()


    fun getConversationId() = conversationResponse.id
    fun loadMessages(): List<MessageUiModel>? = chatFragmentUseCase.messageModelsMapping(conversationResponse.messages)
    fun getUserId() = chatApiRepository.getUserId()
    private fun getUser() = chatApiRepository.getUser()



    private fun onTextMessage() = viewModelScope.launch(Dispatchers.IO) {
        incomingMessageUseCase.onTextMessage().collect { message ->
            val messageModel = chatFragmentUseCase.messageModelMapping(message = message)
            if(messageModel.message.user?.id != getUserId())
                _onMessage.emit(messageModel)
        }
    }

    private fun onImageMessage() = viewModelScope.launch(Dispatchers.IO) {
        incomingMessageUseCase.onImageMessage().collect { message ->
            val messageModel = chatFragmentUseCase.messageModelMapping(message = message)
            if(messageModel.message.user?.id != getUserId())
                _onMessage.emit(messageModel)
        }
    }


    fun createTextMessageModel(text: String): MessageUiModel {
        return MessageUiModel(
            Message(
                uuId = UUID.randomUUID().toString() + System.currentTimeMillis().toString(),
                conversationId = conversationResponse.id,
                sendAt = Date(System.currentTimeMillis()),
                messageContentTypeId = MessageType.TEXT_MESSAGE.key,
                user = getUser(),
                textMessage = TextMessage(
                    text = text
                )
            )
        )
    }

    fun groupMembers(users: List<User>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("You")
        users.forEachIndexed { index, user ->
            if(user.id != getUserId())
                if(users.size > index) {
                    stringBuilder.append(", ")
                    stringBuilder.append(user.userName)
                }
        }
        return stringBuilder.toString()
    }

    fun sendMessageButtonVisibilityState(text: Editable?) {
        text?.let { _sendMessageButtonVisibility.value = it.length > 0 }
    }
}