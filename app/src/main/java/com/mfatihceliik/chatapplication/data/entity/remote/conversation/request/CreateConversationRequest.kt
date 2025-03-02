package com.mfatihceliik.chatapplication.data.entity.remote.conversation.request

import com.mfatihceliik.chatapplication.data.entity.remote.user.User

data class CreateConversationRequest(
    val creatorId: Int,
    val conversationName: String,
    val users: List<User>
)
