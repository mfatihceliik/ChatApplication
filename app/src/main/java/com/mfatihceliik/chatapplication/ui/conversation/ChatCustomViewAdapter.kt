package com.mfatihceliik.chatapplication.ui.conversation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mfatihceliik.chatapplication.data.entity.enums.MessageType
import com.mfatihceliik.chatapplication.data.entity.enums.ReceiverType
import com.mfatihceliik.chatapplication.data.entity.enums.SenderType
import com.mfatihceliik.chatapplication.data.entity.remote.message.ImageMessage
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.message.TextMessage
import com.mfatihceliik.chatapplication.ui.conversation.viewHolders.others.EmptyViewHolder
import com.mfatihceliik.chatapplication.ui.conversation.viewHolders.receiver.ReceiverImageMessageViewHolder
import com.mfatihceliik.chatapplication.ui.conversation.viewHolders.receiver.ReceiverTextMessageViewHolder
import com.mfatihceliik.chatapplication.ui.conversation.viewHolders.sender.SenderImageMessageViewHolder
import com.mfatihceliik.chatapplication.ui.conversation.viewHolders.sender.SenderTextMessageViewHolder
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ChatCustomViewAdapter(
    private val userId: Int,
    private var messageModelList: ArrayList<MessageUiModel> = ArrayList(),
    private var currentMessage: MessageUiModel? = null,
    private var previousMessage: MessageUiModel? = null,
    private var nextMessage: MessageUiModel? = null
): RecyclerView.Adapter<ViewHolder>() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        const val TAG = "ChatCustomViewAdapter"
    }

    fun loadMessage(messages: List<MessageUiModel>?) {
        this.messageModelList.clear()
        this.messageModelList = ArrayList(messages)
        notifyDataSetChanged()
    }

    fun insertMessageItem(message: MessageUiModel) {
        this.messageModelList.add(message)
        val messageListSize = this.messageModelList.size
        notifyItemInserted(messageListSize)
    }

    fun updateMessage(newMessage: Message) {
        val index = findIndexOfMessage(newMessage = newMessage)
        if(index != RecyclerView.NO_POSITION) {
            this.messageModelList[index].message = newMessage
            this.messageModelList[index].message.isSend
            this.messageModelList[index].progressbarVisibility = false
            this.messageModelList[index].doubleTickVisibility = true
            notifyItemChanged(index, this.messageModelList[index])
        }
    }

    private fun findIndexOfMessage(newMessage: Message): Int {
        this.messageModelList.forEachIndexed { index, oldMessage ->
            if(newMessage.uuId == oldMessage.message.uuId) {
                return index
            }

        }
        return RecyclerView.NO_POSITION
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            SenderType.TEXT_MESSAGE_SENDER.key -> {
                SenderTextMessageViewHolder.from(parent = parent)
            }
            ReceiverType.TEXT_MESSAGE_RECEIVER.key -> {
                ReceiverTextMessageViewHolder.from(parent = parent)
            }
            SenderType.IMAGE_MESSAGE_SENDER.key -> {
                SenderImageMessageViewHolder.from(parent = parent)
            }
            ReceiverType.IMAGE_MESSAGE_RECEIVER.key -> {
                ReceiverImageMessageViewHolder.from(parent = parent)
            }
            SenderType.FILE_MESSAGE_SENDER.key -> {
                SenderImageMessageViewHolder.from(parent = parent)
            }
            ReceiverType.FILE_MESSAGE_RECEIVER.key -> {
                ReceiverImageMessageViewHolder.from(parent = parent)
            }
            else -> EmptyViewHolder.from(parent = parent)
        }

    override fun getItemCount(): Int  = this.messageModelList.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textMessage: TextMessage? = currentMessage?.message?.textMessage
        val imageMessage: ImageMessage? = currentMessage?.message?.imageMessage

        when(holder.itemViewType) {
            SenderType.TEXT_MESSAGE_SENDER.key -> {
                val myHolder = holder as SenderTextMessageViewHolder
                myHolder.bind(
                    currentMessage = currentMessage!!,
                    textMessage = textMessage!!,
                    previousMessage = previousMessage,
                    nextMessage = nextMessage
                )

                updateItem(position = position)
            }

            ReceiverType.TEXT_MESSAGE_RECEIVER.key -> {
                val myHolder = holder as ReceiverTextMessageViewHolder
                myHolder.bind(
                    currentMessage = currentMessage!!,
                    textMessage = textMessage!!,
                    previousMessage = previousMessage,
                    nextMessage = nextMessage
                )

                updateItem(position = position)
            }

            SenderType.IMAGE_MESSAGE_SENDER.key -> {
                val myHolder = holder as SenderImageMessageViewHolder
                myHolder.bind(
                    currentMessage = currentMessage!!,
                    imageMessage = imageMessage!!,
                    previousMessage = previousMessage,
                    nextMessage = nextMessage
                )

                updateItem(position = position)
            }

            ReceiverType.IMAGE_MESSAGE_RECEIVER.key -> {
                val myHolder = holder as ReceiverImageMessageViewHolder
                myHolder.bind(
                    currentMessage = currentMessage!!,
                    imageMessage = imageMessage!!,
                    previousMessage = previousMessage
                )

                updateItem(position = position)
            }

            MessageType.EMPTY_MESSAGE.key -> {
                val myHolder = holder as EmptyViewHolder
                myHolder.bindEmptyViewHolder()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        currentMessage = this.messageModelList.getOrNull(position)
        previousMessage = this.messageModelList.getOrNull(position - 1)
        nextMessage = this.messageModelList.getOrNull(position + 1)

        return if(currentMessage == null) {
            MessageType.EMPTY_MESSAGE.key
        }else if(currentMessage!!.message.user?.id == userId) {
            when(currentMessage!!.message.messageContentTypeId) {
                MessageType.TEXT_MESSAGE.key -> SenderType.TEXT_MESSAGE_SENDER.key
                MessageType.IMAGE_MESSAGE.key -> SenderType.IMAGE_MESSAGE_SENDER.key
                MessageType.FILE_MESSAGE.key -> SenderType.FILE_MESSAGE_SENDER.key
                else -> MessageType.EMPTY_MESSAGE.key
            }
        }else {
            when(currentMessage!!.message.messageContentTypeId) {
                MessageType.TEXT_MESSAGE.key -> ReceiverType.TEXT_MESSAGE_RECEIVER.key
                MessageType.IMAGE_MESSAGE.key -> ReceiverType.IMAGE_MESSAGE_RECEIVER.key
                MessageType.FILE_MESSAGE.key -> ReceiverType.FILE_MESSAGE_RECEIVER.key
                else -> MessageType.EMPTY_MESSAGE.key
            }
        }
    }

    private fun updateItem(position: Int) {
        if(previousMessage?.isProfileImageShown == true)
            scope.launch {
                notifyItemChanged(position - 1, previousMessage)
            }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        job.cancel()
    }
}