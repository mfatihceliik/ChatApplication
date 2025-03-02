package com.mfatihceliik.chatapplication.data.remote

import com.mfatihceliik.chatapplication.data.entity.remote.conversation.request.CreateConversationRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.util.BaseSocketDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteSocketSource @Inject constructor(
    private val socketService: SocketService
): BaseSocketDataSource() {

    suspend fun onConversations(userId: Int) = getResult { socketService.onConversations(userId = userId) }
    suspend fun getAllUsers(userId: Int) = getResult { socketService.getAllUsers(userId = userId) }
    suspend fun joinConversations(userId: Int) = getResult { socketService.joinConversations(userId = userId) }
    suspend fun userTyping() = getResult { socketService.userTyping() }

    suspend fun createConversation(createConversationRequest: CreateConversationRequest) = getResult { socketService.createConversation(createConversationRequest = createConversationRequest) }

    suspend fun onTextMessage() = getResult { socketService.onTextMessage() }
    suspend fun onImageMessage() = getResult { socketService.onImageMessage() }

    suspend fun sendTextMessage(textMessageRequest: TextMessageRequest) = getResult { socketService.sendTextMessage(textMessageRequest = textMessageRequest) }
    suspend fun sendImageMessage(imageMessageRequest: ImageMessageRequest) = getResult { socketService.sendImageMessage(imageMessageRequest = imageMessageRequest) }

    suspend fun connectionError(): Flow<Boolean> = socketService.connectionError()
    suspend fun connectionSuccess(): Flow<Boolean> = socketService.connectionSuccess()
    fun reConnect() = socketService.reConnect()
    fun disconnect() = socketService.disconnect()
}