package com.mfatihceliik.chatapplication.data.entity.remote.typing

import com.google.gson.annotations.SerializedName
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

data class Typing(
    @SerializedName("user")
    val user: User?,
    @SerializedName("conversationId")
    val conversationId: Int?,
    @SerializedName("isTyping")
    val isTyping: Boolean?
)