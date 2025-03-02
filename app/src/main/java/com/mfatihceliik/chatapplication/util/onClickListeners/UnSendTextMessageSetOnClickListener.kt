package com.mfatihceliik.chatapplication.util.onClickListeners

import com.mfatihceliik.chatapplication.data.entity.remote.message.Message

interface UnSendTextMessageSetOnClickListener {
    fun onClick(message: Message)
}