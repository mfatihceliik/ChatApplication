package com.mfatihceliik.chatapplication.data.entity.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.mfatihceliik.chatapplication.data.entity.local.Conversation
import com.mfatihceliik.chatapplication.data.entity.local.Message
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

data class ConversationAndMessage (
    @Embedded val conversation: Conversation,
    @Relation(
        entity = Message::class,
        parentColumn = "conversation_id",
        entityColumn = "message_conversationId"
    )
    val messages: List<MessageAndContent>,
    @Relation(
        entity = User::class,
        parentColumn = "conversation_id",
        entityColumn = "user_id",
        associateBy = Junction(
            value = Participant::class,
            parentColumn = "participant_conversationId",
            entityColumn = "participant_userId"
        )
    )
    val users: List<User>
    /*@Relation(
        parentColumn = "conversation_id",
        entityColumn = "message_conversationId",
        associateBy = Junction(Participant::class)
    )
    val users: List<User>*/
)
