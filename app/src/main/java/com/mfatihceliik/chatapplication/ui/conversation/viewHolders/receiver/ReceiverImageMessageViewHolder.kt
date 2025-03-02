package com.mfatihceliik.chatapplication.ui.conversation.viewHolders.receiver

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.message.ImageMessage
import com.mfatihceliik.chatapplication.databinding.ReceiverImageMessageWithProfileImageBinding
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.util.lastMessageTime

class ReceiverImageMessageViewHolder(
    private val binding: ReceiverImageMessageWithProfileImageBinding
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val TAG = "ImageMessageSenderVH"

        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReceiverImageMessageWithProfileImageBinding.inflate(layoutInflater, parent, false)
            return ReceiverImageMessageViewHolder(binding)
        }
    }

    fun bind(
        currentMessage: MessageUiModel,
        imageMessage: ImageMessage,
        previousMessage: MessageUiModel?
    ) {
        binding.imageView.setMessage(text = imageMessage.text ?: "Image") //.message.text = imageMessage.text ?: "Image message"
        //Glide.with(binding.root.context).load(imageMessage.imageUrl).into(binding.imageContentImageView)
        binding.imageView.setSendTime(time = lastMessageTime(currentMessage.message.sendAt)) //.sendTime.text = lastMessageTime(date = currentMessage.sendAt)
        binding.imageView.setOnClickListener {
            Log.v(TAG, currentMessage.toString())
        }
    }

}