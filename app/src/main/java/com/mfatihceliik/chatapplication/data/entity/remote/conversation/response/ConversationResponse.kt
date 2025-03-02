package com.mfatihceliik.chatapplication.data.entity.remote.conversation.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ConversationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("creatorId")
    val creatorId: Int,
    @SerializedName("conversationName")
    val conversationName: String,
    @SerializedName("createdDate")
    val createdDate: Date,
    @SerializedName("groupMembers")
    val groupMembers: List<User>,
    @SerializedName("messages")
    val messages: List<Message>? = null
): Parcelable