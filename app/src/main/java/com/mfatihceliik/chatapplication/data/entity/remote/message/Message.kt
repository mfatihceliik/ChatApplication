package com.mfatihceliik.chatapplication.data.entity.remote.message

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Message(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("uuId")
    val uuId: String? = null,
    @SerializedName("conversationId")
    val conversationId: Int? = null,
    @SerializedName("messageContentTypeId")
    val messageContentTypeId: Int? = null,
    @SerializedName("isSend")
    val isSend: Boolean? = false,
    @SerializedName("sendAt")
    val sendAt: Date? = null,
    @SerializedName("messageContentType")
    val messageContentType: MessageContentType? = null,
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("textMessage")
    val textMessage: TextMessage? = null,
    @SerializedName("imageMessage")
    val imageMessage: ImageMessage? = null
): Parcelable
