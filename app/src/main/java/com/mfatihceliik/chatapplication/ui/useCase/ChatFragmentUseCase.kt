package com.mfatihceliik.chatapplication.ui.useCase

import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.ui.mapper.MessageModelMapper
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import javax.inject.Inject

class ChatFragmentUseCase @Inject constructor(
    private val chatApiRepository: ChatApiRepository,
    private val socketRepository: ChatApiSocketRepository,

) {
    companion object {
        private const val TAG = "ChatFragmentUseCase"
    }

    fun messageModelsMapping(messages: List<Message>?): List<MessageUiModel>? {
        val model = MessageModelMapper()
        val messageModel = messages?.map {
            model.from(it)
        }
        return messageModel
    }

    fun messageModelMapping(message: Message): MessageUiModel {
        return MessageModelMapper().from(message)
    }
}