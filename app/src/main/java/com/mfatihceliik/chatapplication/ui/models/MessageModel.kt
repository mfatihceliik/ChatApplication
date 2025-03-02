package com.mfatihceliik.chatapplication.ui.models

import android.os.Parcelable
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageUiModel(
    var message: Message,
    var isProfileImageShown: Boolean = true,
    var isUserNameShown: Boolean = true,
    var progressbarVisibility: Boolean = true,
    var doubleTickVisibility: Boolean? = null
): Parcelable