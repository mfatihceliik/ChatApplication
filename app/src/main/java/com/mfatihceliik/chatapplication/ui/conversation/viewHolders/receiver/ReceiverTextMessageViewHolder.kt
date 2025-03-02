package com.mfatihceliik.chatapplication.ui.conversation.viewHolders.receiver

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.message.TextMessage
import com.mfatihceliik.chatapplication.databinding.ReceiverTextMessageWithProfileImageBinding
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.util.lastMessageTime
class ReceiverTextMessageViewHolder(
    private val binding: ReceiverTextMessageWithProfileImageBinding
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val TAG = "ReceiverTextMessageVH"
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReceiverTextMessageWithProfileImageBinding.inflate(layoutInflater, parent, false)
            return ReceiverTextMessageViewHolder(binding)
        }
    }

    fun bind(
        currentMessage: MessageUiModel,
        textMessage: TextMessage,
        previousMessage: MessageUiModel?,
        nextMessage: MessageUiModel?
    ) {

        isProfileImageShown(currentMessage = currentMessage, nextMessage = nextMessage, previousMessage = previousMessage)
        isUserNameShown(currentMessage = currentMessage, previousMessage = previousMessage)

        binding.textView.setSendTime(time = lastMessageTime(currentMessage.message.sendAt)) //.sendTime.text = lastMessageTime(currentMessage.sendAt)
        currentMessage.message.user?.let { binding.textView.setSenderName(name = it.userName) } // .senderName.text = currentMessage.user.userName
        textMessage.text?.let { binding.textView.setMessage(text = it) } //.message.text = textMessage.text


        binding.profileView.visibility = if(currentMessage.isProfileImageShown) View.VISIBLE else View.GONE
        binding.textView.setSenderNameVisibility(if(currentMessage.isUserNameShown) true else false)

        binding.textView.setOnClickListener {
            Log.v(TAG, currentMessage.toString())
        }

//        Log.v(TAG,"Current")
//        Log.v(TAG, currentMessage.toString())
//        Log.v(TAG,"Previous")
//        Log.v(TAG, previousMessage.toString())
//        Log.v(TAG,"Next")
//        Log.v(TAG, nextMessage.toString())
    }


    private fun previousMessageProfileImageShown() {

    }

    private fun isUserNameShown(currentMessage: MessageUiModel, previousMessage: MessageUiModel?) =
        if(currentMessage.message.user?.id == previousMessage?.message?.user?.id) {
            currentMessage.isUserNameShown = false
            previousMessage?.isUserNameShown = false
        }
        else
            currentMessage.isUserNameShown = true

    private fun isProfileImageShown(currentMessage: MessageUiModel, nextMessage: MessageUiModel?, previousMessage: MessageUiModel?) =
        if(currentMessage.message.user?.id == previousMessage?.message?.user?.id) {
            currentMessage.isProfileImageShown = false
            //previousMessage?.isProfileImageShown = false
        }
        else
            currentMessage.isProfileImageShown = true



    /*private fun checkProfileVisibility(currentMessage: MessageUiModel, nextMessage: MessageUiModel?): Int =
        if(currentMessage.message.user?.id == nextMessage?.message?.user?.id) View.GONE else View.VISIBLE

    private fun checkUserNameVisibility(currentMessage: MessageUiModel, previousMessage: MessageUiModel?): Int =
        if(currentMessage.message.user?.id == previousMessage?.message?.user?.id) View.GONE else View.VISIBLE*/


    private fun progressBarVisibility(message: MessageUiModel): Int =
        if (message.message.isSend == true) View.INVISIBLE else View.VISIBLE

}

