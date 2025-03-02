package com.mfatihceliik.chatapplication.data

import com.mfatihceliik.chatapplication.data.entity.remote.conversation.request.CreateConversationRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.data.remote.RemoteSocketSource
import com.mfatihceliik.chatapplication.util.performNetworkOperationByFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatApiSocketRepository @Inject constructor(
    private val remoteSocketSource: RemoteSocketSource,
) {

    fun onConversations(userId: Int) = performNetworkOperationByFlow { remoteSocketSource.onConversations(userId = userId) }
    fun getAllUsers(userId: Int) = performNetworkOperationByFlow { remoteSocketSource.getAllUsers(userId = userId) }
    fun joinConversations(userId: Int) = performNetworkOperationByFlow { remoteSocketSource.joinConversations(userId = userId) }
    fun userTyping() = performNetworkOperationByFlow { remoteSocketSource.userTyping() }

    fun createConversation(createConversationRequest: CreateConversationRequest) = performNetworkOperationByFlow { remoteSocketSource.createConversation(createConversationRequest = createConversationRequest) }

    fun onTextMessage() = performNetworkOperationByFlow { remoteSocketSource.onTextMessage() }
    fun onImageMessage() = performNetworkOperationByFlow { remoteSocketSource.onImageMessage() }

    suspend fun sendTextMessage(textMessageRequest: TextMessageRequest) = performNetworkOperationByFlow { remoteSocketSource.sendTextMessage(textMessageRequest = textMessageRequest) }
    suspend fun sendImageMessage(imageMessageRequest: ImageMessageRequest) = performNetworkOperationByFlow { remoteSocketSource.sendImageMessage(imageMessageRequest = imageMessageRequest) }

    suspend fun connectionError(): Flow<Boolean> = remoteSocketSource.connectionError()
    suspend fun connectionSuccess(): Flow<Boolean> = remoteSocketSource.connectionSuccess()
    fun reConnect() = remoteSocketSource.reConnect()
    fun disconnect() = remoteSocketSource.disconnect()
}