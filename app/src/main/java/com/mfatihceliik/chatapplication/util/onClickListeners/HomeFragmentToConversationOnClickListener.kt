package com.mfatihceliik.chatapplication.util.onClickListeners

import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse


interface HomeFragmentToConversationOnClickListener {
    fun onClick(conversationResponse: ConversationResponse)
}