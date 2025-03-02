package com.mfatihceliik.chatapplication.data.entity.base

import com.google.gson.annotations.SerializedName


data class SocketResponse<out T> (
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean
)