package com.mfatihceliik.chatapplication

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.typing.Typing
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.ui.useCase.SendMessageUseCase
import com.mfatihceliik.chatapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val chatApiRepository: ChatApiRepository,
    private val socketRepository: ChatApiSocketRepository,
    private val sendMessageUseCase: SendMessageUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {

    companion object {
        private const val TAG = "SharedViewModel"
    }

    init {
        joinConversations()
        userTyping()
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "onCleared")
    }


    private val _callBackMessage = MutableSharedFlow<Message>()
    val callBackMessage: SharedFlow<Message> = _callBackMessage.asSharedFlow()

    private val _addMessageToRecyclerView = MutableSharedFlow<MessageUiModel>(replay = 1)
    val addMessageToRecyclerView: SharedFlow<MessageUiModel> = _addMessageToRecyclerView.asSharedFlow()

    private val _userTyping = MutableStateFlow<Typing?>(null)
    val userTyping: SharedFlow<Typing?> = _userTyping.asStateFlow()

    private val _joinConversation = MutableSharedFlow<List<Participant>>()
    val joinConversation: SharedFlow<List<Participant>> = _joinConversation.asSharedFlow()

    private val _conversationResponse = MutableLiveData<ConversationResponse>()
    val conversationResponse: LiveData<ConversationResponse> = _conversationResponse

    private val _conversationId = MutableLiveData<Int>()
    val conversationId: LiveData<Int> = _conversationId

    private fun getUserId(): Int = chatApiRepository.getUser().id
    //private suspend fun insertMessageLocalDb(message: Message) = chatApiRepository.insertMessage(message = message)
    //private suspend fun updateMessage(message: Message) = chatApiRepository.updateMessage(message = message)


    fun setConversation(conversationResponse: ConversationResponse) {
        _conversationResponse.value = conversationResponse
    }

    fun setConversationId(conversationId: Int) {
       _conversationId.value = conversationId
    }

    fun addMessageToRecyclerView(message: MessageUiModel) = viewModelScope.launch(Dispatchers.IO) {
        _addMessageToRecyclerView.emit(message)
    }

    fun callBackMessage(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        _callBackMessage.emit(message)
    }

    fun saveMessageToLocalDb(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        //val insertMessage = async { insertMessageLocalDb(message = message) }
        //insertMessage.await()
    }

    fun updateMessageInLocalDb(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        //val insertMessage = async { updateMessage(message = message) }
        //insertMessage.await()
    }

    fun sendImageMessage(message: MessageUiModel) = viewModelScope.launch(Dispatchers.IO) {
        val imageMessageRequest = ImageMessageRequest(
            uuId = message.message.uuId!!,
            conversationId = message.message.conversationId!!,
            userId = message.message.user?.id!!,
            messageContentTypeId = message.message.messageContentTypeId!!,
            imageBlob = message.message.imageMessage?.imageBlob!!,
            text = message.message.imageMessage!!.text
        )
        sendMessageUseCase.sendImageMessage(imageMessageRequest = imageMessageRequest).collect { message ->
            //val messageUIModel = chatFragmentUseCase.messageModelMapping(message = message)
            _callBackMessage.emit(message)
        }
    }

    fun sendTextMessage(message: MessageUiModel) = viewModelScope.launch(Dispatchers.IO) {
        val textMessageRequest = TextMessageRequest(
            uuId = message.message.uuId!!,
            conversationId = message.message.conversationId!!,
            userId = message.message.user?.id!!,
            messageContentTypeId = message.message.messageContentTypeId!!,
            text = message.message.textMessage!!.text!!
        )
        sendMessageUseCase.sendTextMessage(textMessageRequest = textMessageRequest).collect { message ->
            _callBackMessage.emit(message)
        }
    }


    fun joinConversations() = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " joinConversations")
        socketRepository.joinConversations(getUserId()).collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, "joinConversations LOADING")
                Resource.Status.SUCCESS -> it.data?.let { participants ->
                    withContext(Dispatchers.Main) {
                        Log.v(TAG, Thread.currentThread().name + " joinConversations")
                        _joinConversation.emit(participants)
                    }
                }
                Resource.Status.ERROR -> {
                    Log.v(TAG, "joinConversations ERROR")
                    Log.v(TAG, it.message.toString())
                }
            }
        }
    }
    private fun userTyping() = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " userTyping")
        socketRepository.userTyping().collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, "userTyping LOADING")
                Resource.Status.SUCCESS -> it.data?.let { typing ->
                    withContext(Dispatchers.Main) {
                        Log.v(TAG, Thread.currentThread().name + " userTyping")
                        _userTyping.emit(typing)
                    }
                }
                Resource.Status.ERROR -> Log.v(TAG, "userTyping ERROR")
            }
        }
    }

    /*fun separateNotSentMessages(messages: List<Message>) = viewModelScope.launch {
        Log.v(TAG, Thread.currentThread().name + " separateNotSentMessages")
        messages.forEach { notSentMessages ->
            when(notSentMessages.messageContentTypeId) {
                MessageType.TEXT_MESSAGE.key -> {
                    reSendTextMessage(notSentMessages = notSentMessages)
                }
                MessageType.IMAGE_MESSAGE.key -> {
                    reSendImageMessage(notSentMessages = notSentMessages)
                }
                MessageType.FILE_MESSAGE.key -> {

                }
            }
            delay(1200L)
        }
    }
    private suspend fun reSendTextMessage(notSentMessages: TextMessageModel) {
        val textMessageRequest = TextMessageRequest(
            uuid = notSentMessages.uuId,
            conversationId = notSentMessages.conversationId!!,
            userId = notSentMessages.user.id,
            messageContentTypeId = notSentMessages.messageContentTypeId,
            text = notSentMessages.text
        )
        sendMessageUseCase.invoke(message = notSentMessages, textMessageRequest = textMessageRequest).collect { message ->
            //_sendTextMessageResponseSharedFlow.emit(message)
            _callBackMessage.emit(message)
        }
    }
    private suspend fun reSendImageMessage(notSentMessages: ImageMessageModel) {
        val fileConverter = FileConverter(context = context)
        val uri = notSentMessages.imageUri.toUri()
        val imageAsBitmap = fileConverter.getBitmapFromUri(uri = uri)
        val byteArrayFromBitmap = fileConverter.byteArrayFromBitmap(bitmap = imageAsBitmap)
        val imageMessageRequest = ImageMessageRequest(
            uuid = notSentMessages.uuId,
            conversationId = notSentMessages.conversationId!!,
            userId = notSentMessages.user.id,
            messageContentTypeId = notSentMessages.messageContentTypeId,
            imageBlob = byteArrayFromBitmap,
            text = notSentMessages.text
        )
        sendMessageUseCase.invoke(message = notSentMessages, imageMessageRequest = imageMessageRequest).collect { message ->
            //_sendImageMessageResponseSharedFlow.emit(message)
            _callBackMessage.emit(message)
        }
    }*/
}