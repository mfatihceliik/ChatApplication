package com.mfatihceliik.chatapplication.data.entity.remote.user

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: String,
    @SerializedName("data")
    val user: User,
    @SerializedName("token")
    val token: String
)
