package com.mfatihceliik.chatapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mfatihceliik.chatapplication.data.entity.enums.MessageType
import com.mfatihceliik.chatapplication.data.entity.local.Conversation
import com.mfatihceliik.chatapplication.data.entity.local.ImageMessage
import com.mfatihceliik.chatapplication.data.entity.local.Message
import com.mfatihceliik.chatapplication.data.entity.local.TextMessage
import com.mfatihceliik.chatapplication.data.entity.local.relations.ConversationAndMessage
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.message.MessageContentType
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatAppDao {

    // USER
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Update
    suspend fun updateUsers(users: List<User>)

    @Delete
    suspend fun deleteUser(user: User)

    // MESSAGE CONTENT TYPE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageContentType(messageContentType: MessageContentType)


    // PARTICIPANT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<Participant>)

    @Update
    suspend fun updateParticipants(participants: List<Participant>)

    @Delete
    suspend fun deleteParticipants(participant: Participant)

    // Conversation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<Conversation>): List<Long>

    @Update
    suspend fun updateConversation(conversation: Conversation)

    @Delete
    suspend fun deleteConversation(conversation: Conversation)

    /*
    * SELECT * FROM conversations INNER JOIN participants ON conversations.id = participants.conversationId
    * */
    @Transaction
    @Query("SELECT * FROM conversations")
    fun getConversationsWithMessages(): Flow<List<ConversationAndMessage>>

    // MESSAGE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message): Long

    @Update
    suspend fun updateMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

    // TEXT MESSAGE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTextMessage(textMessage: TextMessage)

    @Update
    suspend fun updateTextMessage(textMessage: TextMessage)

    @Delete
    suspend fun deleteTextMessage(textMessage: TextMessage)

    // IMAGE MESSAGE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageMessage(imageMessage: ImageMessage)

    @Update
    suspend fun updateImageMessage(imageMessage: ImageMessage)


    @Delete
    suspend fun deleteImageMessage(imageMessage: ImageMessage)


    @Transaction
    suspend fun addConversation(conversationResponse: ConversationResponse) {
        val conversation = Conversation(
            id = conversationResponse.id,
            conversationName = conversationResponse.conversationName,
            creatorId = conversationResponse.creatorId,
            createdDate = conversationResponse.createdDate
        )
        insertConversation(conversation = conversation)
        conversationResponse.messages?.forEach { message ->
            val msg = Message(
                id = message.id!!,
                //uuId = message.uuId,
                conversationId = message.conversationId!!,
                userId = message.user?.id!!,
                messageContentTypeId = message.messageContentTypeId!!,
                isSend = message.isSend!!,
                sendAt = message.sendAt!!
            )
            insertMessage(message = msg)
            when(message.messageContentTypeId) {
                MessageType.TEXT_MESSAGE.key -> {
                    val textMessage = TextMessage(
                        id = message.id,
                        text = message.textMessage!!.text!!
                    )
                    insertTextMessage(textMessage = textMessage)
                }
                MessageType.IMAGE_MESSAGE.key -> {
                    val imageMessage = ImageMessage(
                        id = message.id,
                        imageUrl = message.imageMessage!!.imageUrl!!,
                        imageUri = message.imageMessage.imageUri!!,
                        imageBlob = message.imageMessage.imageBlob,
                        text = message.imageMessage.text
                    )
                    insertImageMessage(imageMessage = imageMessage)
                }
            }
        }
    }
}