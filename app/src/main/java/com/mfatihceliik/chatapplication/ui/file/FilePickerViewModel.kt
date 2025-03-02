package com.mfatihceliik.chatapplication.ui.file

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.enums.MessageType
import com.mfatihceliik.chatapplication.data.entity.remote.message.ImageMessage
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.ui.useCase.ChatFragmentUseCase
import com.mfatihceliik.chatapplication.ui.useCase.SendMessageUseCase
import com.mfatihceliik.chatapplication.util.FileConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FilePickerViewModel @Inject constructor(
    private val socketRepository: ChatApiSocketRepository,
    private val chatApiRepository: ChatApiRepository,
    @ApplicationContext private val context: Context,
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatFragmentUseCase: ChatFragmentUseCase,
): ViewModel() {

    companion object {
        const val TAG = "FilePickerViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "onCleared")
    }

    private val _conversationId = MutableLiveData<Int>()

    private fun getUser(): User = chatApiRepository.getUser()
    private fun getUserId(): Int = chatApiRepository.getUserId()

    fun setConversationId(conversationId: Int) {
        _conversationId.value = conversationId
    }

    fun createImageMessage(text: String?, uri: Uri): MessageUiModel {
        val fileConverter = FileConverter(context = context)
        val imageAsBitmap = fileConverter.getBitmapFromUri(uri = uri)
        val byteArrayFromBitmap = fileConverter.byteArrayFromBitmap(bitmap = imageAsBitmap)

        return MessageUiModel(
            Message(
                uuId = UUID.randomUUID().toString() + System.currentTimeMillis().toString(),
                conversationId = _conversationId.value,
                messageContentTypeId = MessageType.IMAGE_MESSAGE.key,
                sendAt = Date(System.currentTimeMillis()),
                user = getUser(),
                imageMessage = ImageMessage(
                    imageUrl = "",
                    imageUri = uri.toString(),
                    imageBlob = byteArrayFromBitmap,
                    text = text
                )
            )
        )
    }

    /*fun createImageMessage(text: String?, uri: Uri): Message {
        val fileConverter = FileConverter(context = context)
        val imageAsBitmap = fileConverter.getBitmapFromUri(uri = uri)
        val byteArrayFromBitmap = fileConverter.byteArrayFromBitmap(bitmap = imageAsBitmap)
        return Message(
            id = Int.MIN_VALUE,
            uuId = UUID.randomUUID().toString() + System.currentTimeMillis().toString(),
            conversationId = this._conversationId.value!!,
            messageContentTypeId = MessageType.IMAGE_MESSAGE.key,
            user = getUser(),
            isSend = false,
            sendAt = Date(System.currentTimeMillis()),
            messageContentType = MessageContentType(SenderType.TEXT_MESSAGE_SENDER.key, SenderType.TEXT_MESSAGE_SENDER.value),
            imageMessage = ImageMessage(
                id = Int.MIN_VALUE,
                imageUrl = "",
                imageUri = uri.toString(),
                imageBlob = byteArrayFromBitmap,
                text = text
            )
        )
    }*/
}