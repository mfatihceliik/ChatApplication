package com.mfatihceliik.chatapplication.ui.conversation

import androidx.recyclerview.widget.DiffUtil
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message

class MessagesDiffUtil(
    private val oldList: ArrayList<Message> = ArrayList(),
    private val newList: ArrayList<Message> = ArrayList()
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].textMessage?.id != newList[newItemPosition].textMessage?.id -> false
            oldList[oldItemPosition].textMessage?.text != newList[newItemPosition].textMessage?.text -> false
            oldList[oldItemPosition].imageMessage?.id != newList[newItemPosition].imageMessage?.id -> false
            oldList[oldItemPosition].imageMessage?.imageUrl != newList[newItemPosition].imageMessage?.imageUrl -> false
            else -> true
        }
    }
}