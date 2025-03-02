package com.mfatihceliik.chatapplication.ui.conversation.viewHolders.sender

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.message.TextMessage
import com.mfatihceliik.chatapplication.databinding.SenderTextMessageWithProfileImageBinding
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.util.lastMessageTime
class SenderTextMessageViewHolder (
    private val binding: SenderTextMessageWithProfileImageBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val TAG = "TMSenderViewHolder"
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SenderTextMessageWithProfileImageBinding.inflate(layoutInflater, parent, false)
            return SenderTextMessageViewHolder(binding)
        }
    }

    fun bind(
        currentMessage: MessageUiModel,
        textMessage: TextMessage,
        previousMessage: MessageUiModel?,
        nextMessage: MessageUiModel?
    ) {

        isProfileImageShown(currentMessage = currentMessage, nextMessage = nextMessage, previousMessage = previousMessage)
        checkMessageSent(currentMessage = currentMessage)

        binding.textView.setSendTime(text = lastMessageTime(date = currentMessage.message.sendAt)) // sendTime.text = lastMessageTime(date = currentMessage.sendAt)
        binding.textView.setMessageText(text = textMessage.text) //.message.text = textMessage.text

        binding.profileView.visibility = if(currentMessage.isProfileImageShown) View.VISIBLE else View.GONE
        binding.textView.setProgressBarVisibility(currentMessage.message.isSend)
        binding.textView.setDoubleTickVisibility(currentMessage.doubleTickVisibility)

        binding.textView.setOnClickListener {
            Log.v(TAG, currentMessage.toString())
        }
    }


    private fun checkMessageSent(currentMessage: MessageUiModel) {
        if(currentMessage.message.isSend == true) {
            currentMessage.doubleTickVisibility = true
        }else {
            currentMessage.doubleTickVisibility = false
        }
    }

    private fun isProfileImageShown(currentMessage: MessageUiModel, nextMessage: MessageUiModel?, previousMessage: MessageUiModel?) =
        if(currentMessage.message.user?.id == previousMessage?.message?.user?.id) {
            currentMessage.isProfileImageShown = false
            //previousMessage?.isProfileImageShown = false
        }
        else
            currentMessage.isProfileImageShown = true

}

