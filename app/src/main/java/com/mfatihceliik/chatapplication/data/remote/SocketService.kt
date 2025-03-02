package com.mfatihceliik.chatapplication.data.remote

import com.mfatihceliik.chatapplication.data.entity.remote.typing.Typing
import com.mfatihceliik.chatapplication.data.entity.base.SocketResponse
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.request.CreateConversationRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import kotlinx.coroutines.flow.Flow

interface SocketService {

    suspend fun onConversations(userId: Int): Flow<SocketResponse<List<ConversationResponse>>>
    suspend fun getAllUsers(userId: Int): Flow<SocketResponse<List<User>>>
    suspend fun joinConversations(userId: Int): Flow<SocketResponse<List<Participant>>>
    suspend fun userTyping(): Flow<SocketResponse<Typing>>

    suspend fun createConversation(createConversationRequest: CreateConversationRequest): Flow<SocketResponse<ConversationResponse>>

    suspend fun onTextMessage(): Flow<SocketResponse<Message>>
    suspend fun onImageMessage(): Flow<SocketResponse<Message>>

    suspend fun sendTextMessage(textMessageRequest: TextMessageRequest): Flow<SocketResponse<Message>>
    suspend fun sendImageMessage(imageMessageRequest: ImageMessageRequest): Flow<SocketResponse<Message>>

    suspend fun connectionError(): Flow<Boolean>
    suspend fun connectionSuccess(): Flow<Boolean>
    fun reConnect()
    fun disconnect()

    fun establishConnection()
}
