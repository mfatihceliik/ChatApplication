package com.mfatihceliik.chatapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.enums.MessageType
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.databinding.ItemConversationBinding
import com.mfatihceliik.chatapplication.util.lastMessageTime
import com.mfatihceliik.chatapplication.util.onClickListeners.HomeFragmentToConversationOnClickListener

class ConversationViewHolder(private val binding: ItemConversationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ConversationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemConversationBinding.inflate(layoutInflater, parent, false)
            return ConversationViewHolder(binding = binding)
        }
    }

    fun bind(
        conversationResponse: ConversationResponse,
        message: Message?,
        homeFragmentToConversationOnClickListener: HomeFragmentToConversationOnClickListener?
    ) {

        binding.conversationTitle.text = conversationResponse.conversationName
        binding.lastMessageSenderTextView.text = lastMessageSenderName(user = message?.user)
        binding.lastMessageTextView.text =
            lastMessageText(message = message)
        binding.conversationCardView.setOnClickListener {
            homeFragmentToConversationOnClickListener?.onClick(conversationResponse = conversationResponse)
        }
        binding.date.text = message?.sendAt?.let { lastMessageTime(date = it) }
    }

    private fun lastMessageSenderName(user: User?): String {
        return  user?.userName ?: ""
    }

    private fun lastMessageText(message: Message?): String {
        return when(message?.messageContentTypeId) {
            MessageType.TEXT_MESSAGE.key -> {
                message.textMessage!!.text ?: ""
            }
            MessageType.IMAGE_MESSAGE.key -> {
                message.imageMessage?.text ?: " Image"
            }
            else -> ""
        }
    }
}