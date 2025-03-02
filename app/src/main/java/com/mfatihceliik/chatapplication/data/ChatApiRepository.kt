package com.mfatihceliik.chatapplication.data

import com.mfatihceliik.chatapplication.data.entity.local.Conversation
import com.mfatihceliik.chatapplication.data.entity.local.ImageMessage
import com.mfatihceliik.chatapplication.data.entity.local.TextMessage
import com.mfatihceliik.chatapplication.data.entity.local.relations.ConversationAndMessage
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.message.MessageContentType
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginRequest
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.data.local.LocalDataSource
import com.mfatihceliik.chatapplication.data.remote.RemoteDataSource
import com.mfatihceliik.chatapplication.util.performNetworkOperation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatApiRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun login(loginRequest: LoginRequest) = performNetworkOperation(
        call = {
            remoteDataSource.login(loginRequest)
        }, saveToken = {
            localDataSource.saveToken(it)
        }
    )

    fun getUserId(): Int = localDataSource.getUser()!!.id
    fun getUserName(): String = localDataSource.getUser()!!.userName
    fun getUser(): User = localDataSource.getUser()!!

    fun saveUser(user: User?) {
        localDataSource.saveUser(user = user)
    }

    // USER
    suspend fun insertUser(user: User) = localDataSource.insertUser(user = user)
    suspend fun insertUsers(users: List<User>) = localDataSource.insertUsers(users = users)
    suspend fun updateUser(user: User) = localDataSource.updateUser(user = user)
    suspend fun updateUsers(users: List<User>) = localDataSource.updateUsers(users = users)
    suspend fun deleteUser(user: User) = localDataSource.deleteUser(user = user)

    // MESSAGE CONTENT TYPE
    suspend fun insertMessageContentType(messageContentType: MessageContentType) = localDataSource.insertMessageContentType(messageContentType = messageContentType)

    // PARTICIPANT
    suspend fun insertParticipants(participants: List<Participant>) = localDataSource.insertParticipants(participants = participants)
    suspend fun updateParticipants(participants: List<Participant>) = localDataSource.updateParticipants(participants = participants)
    suspend fun deleteParticipants(participant: Participant) = localDataSource.deleteParticipants(participant = participant)

    // Conversation
    suspend fun insertConversation(conversation: Conversation): Long = localDataSource.insertConversation(conversation = conversation)
    suspend fun insertConversations(conversations: List<Conversation>): List<Long> = localDataSource.insertConversations(conversations = conversations)
    suspend fun updateConversation(conversation: Conversation) = localDataSource.updateConversation(conversation = conversation)
    suspend fun deleteConversation(conversation: Conversation) = localDataSource.deleteConversation(conversation = conversation)

    suspend fun addConversation(conversationResponse: ConversationResponse) = localDataSource.addConversation(conversationResponse = conversationResponse)
    fun getConversationsWithMessages(): Flow<List<ConversationAndMessage>> = localDataSource.getConversationsWithMessages()

    // MESSAGE
    suspend fun insertMessage(message: com.mfatihceliik.chatapplication.data.entity.local.Message): Long = localDataSource.insertMessage(message = message)
    suspend fun updateMessage(message: com.mfatihceliik.chatapplication.data.entity.local.Message) = localDataSource.updateMessage(message = message)
    suspend fun deleteMessage(message: com.mfatihceliik.chatapplication.data.entity.local.Message) = localDataSource.deleteMessage(message = message)

    // TEXT MESSAGE
    suspend fun insertTextMessage(textMessage: TextMessage) = localDataSource.insertTextMessage(textMessage = textMessage)
    suspend fun updateTextMessage(textMessage: TextMessage) = localDataSource.updateTextMessage(textMessage = textMessage)
    suspend fun deleteTextMessage(textMessage: TextMessage) = localDataSource.deleteTextMessage(textMessage = textMessage)

    // IMAGE MESSAGE
    suspend fun insertImageMessage(imageMessage: ImageMessage) = localDataSource.insertImageMessage(imageMessage = imageMessage)
    suspend fun updateImageMessage(imageMessage: ImageMessage) = localDataSource.updateImageMessage(imageMessage = imageMessage)
    suspend fun deleteImageMessage(imageMessage: ImageMessage) = localDataSource.deleteImageMessage(imageMessage = imageMessage)
}