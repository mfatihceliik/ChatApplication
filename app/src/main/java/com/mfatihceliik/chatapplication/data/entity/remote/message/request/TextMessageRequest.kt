package com.mfatihceliik.chatapplication.data.entity.remote.message.request

data class TextMessageRequest(
    val uuId: String,
    val conversationId: Int,
    val userId: Int,
    val messageContentTypeId: Int,
    val text: String,
)
