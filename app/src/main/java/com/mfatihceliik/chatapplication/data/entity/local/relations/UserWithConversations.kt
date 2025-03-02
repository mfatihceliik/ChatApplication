package com.mfatihceliik.chatapplication.data.entity.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mfatihceliik.chatapplication.data.entity.local.ImageMessage
import com.mfatihceliik.chatapplication.data.entity.local.Message
import com.mfatihceliik.chatapplication.data.entity.local.TextMessage
import com.mfatihceliik.chatapplication.data.entity.remote.message.MessageContentType

data class MessageAndContent (
    @Embedded
    val message: Message,
    @Relation(
        parentColumn = "message_id",
        entityColumn = "textMessage_id"
    )
    val textMessage: TextMessage? = null,
    @Relation(
        parentColumn = "message_id",
        entityColumn = "imageMessage_id"
    )
    val imageMessage: ImageMessage? = null,
    @Relation(
        parentColumn = "message_messageContentTypeId",
        entityColumn = "messageContentType_id"
    )
    val messageContentType: MessageContentType
)

/*data class UserAndConversations(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "conversationId",
        associateBy = Junction(Participant::class)
    )
    val conversations: List<ConversationAndMessages>
)*/
