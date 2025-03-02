package com.mfatihceliik.chatapplication.data.local

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
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val chatAppDao: ChatAppDao
) {
    fun saveToken(token: String?) {
        sharedPreferenceManager.saveToken(token = token)
    }
    fun getToken(): String? {
        return sharedPreferenceManager.getToken()
    }
    fun saveUser(user: User?) {
        sharedPreferenceManager.saveUser(user = user)
    }
    fun getUser(): User? {
        return sharedPreferenceManager.getUser()
    }

    // USER
    suspend fun insertUser(user: User) = chatAppDao.insertUser(user = user)
    suspend fun insertUsers(users: List<User>) = chatAppDao.insertUsers(users = users)
    suspend fun updateUser(user: User) = chatAppDao.updateUser(user = user)
    suspend fun updateUsers(users: List<User>) = chatAppDao.updateUsers(users = users)
    suspend fun deleteUser(user: User) = chatAppDao.deleteUser(user = user)

    // MESSAGE CONTENT TYPE
    suspend fun insertMessageContentType(messageContentType: MessageContentType) = chatAppDao.insertMessageContentType(messageContentType = messageContentType)

    // PARTICIPANT
    suspend fun insertParticipants(participants: List<Participant>) = chatAppDao.insertParticipants(participants = participants)
    suspend fun updateParticipants(participants: List<Participant>) = chatAppDao.updateParticipants(participants = participants)
    suspend fun deleteParticipants(participant: Participant) = chatAppDao.deleteParticipants(participant = participant)

    // Conversation
    suspend fun insertConversation(conversation: Conversation): Long = chatAppDao.insertConversation(conversation = conversation)
    suspend fun insertConversations(conversations: List<Conversation>): List<Long> = chatAppDao.insertConversations(conversations = conversations)
    suspend fun updateConversation(conversation: Conversation) = chatAppDao.updateConversation(conversation = conversation)
    suspend fun deleteConversation(conversation: Conversation) = chatAppDao.deleteConversation(conversation = conversation)

    suspend fun addConversation(conversationResponse: ConversationResponse) = chatAppDao.addConversation(conversationResponse = conversationResponse)
    fun getConversationsWithMessages(): Flow<List<ConversationAndMessage>> = chatAppDao.getConversationsWithMessages()

    // MESSAGE
    suspend fun insertMessage(message: Message): Long = chatAppDao.insertMessage(message = message)
    suspend fun updateMessage(message: Message) = chatAppDao.updateMessage(message = message)
    suspend fun deleteMessage(message: Message) = chatAppDao.deleteMessage(message = message)

    // TEXT MESSAGE
    suspend fun insertTextMessage(textMessage: TextMessage) = chatAppDao.insertTextMessage(textMessage = textMessage)
    suspend fun updateTextMessage(textMessage: TextMessage) = chatAppDao.updateTextMessage(textMessage = textMessage)
    suspend fun deleteTextMessage(textMessage: TextMessage) = chatAppDao.deleteTextMessage(textMessage = textMessage)

    // IMAGE MESSAGE
    suspend fun insertImageMessage(imageMessage: ImageMessage) = chatAppDao.insertImageMessage(imageMessage = imageMessage)
    suspend fun updateImageMessage(imageMessage: ImageMessage) = chatAppDao.updateImageMessage(imageMessage = imageMessage)
    suspend fun deleteImageMessage(imageMessage: ImageMessage) = chatAppDao.deleteImageMessage(imageMessage = imageMessage)
}