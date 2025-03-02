package com.mfatihceliik.chatapplication.data.entity.remote.message

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextMessage(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("text")
    val text: String? = null,
): Parcelable