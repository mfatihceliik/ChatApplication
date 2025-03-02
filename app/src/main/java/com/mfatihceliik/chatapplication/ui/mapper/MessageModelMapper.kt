package com.mfatihceliik.chatapplication.ui.mapper

import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel

class MessageModelMapper: Mapper<Message, MessageUiModel> {
    override fun from(from: Message): MessageUiModel {
        return MessageUiModel(
            Message(
                id = from.id,
                uuId = from.uuId,
                conversationId = from.conversationId,
                messageContentType = from.messageContentType,
                isSend = from.isSend,
                sendAt = from.sendAt,
                messageContentTypeId = from.messageContentTypeId,
                user = from.user,
                textMessage = from.textMessage,
                imageMessage = from.imageMessage
            )
        )
    }
}