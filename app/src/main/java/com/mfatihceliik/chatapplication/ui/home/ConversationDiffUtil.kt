package com.mfatihceliik.chatapplication.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse

class ConversationDiffUtil(
    private val oldList: ArrayList<ConversationResponse> = ArrayList(),
    private val newList: ArrayList<ConversationResponse> = ArrayList()
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
            oldList[oldItemPosition].conversationName != newList[newItemPosition].conversationName -> false
            oldList[oldItemPosition].creatorId != newList[newItemPosition].creatorId -> false
            else -> true
        }
    }

}