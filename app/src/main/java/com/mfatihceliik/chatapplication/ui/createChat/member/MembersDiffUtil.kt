package com.mfatihceliik.chatapplication.ui.createChat.member

import androidx.recyclerview.widget.DiffUtil
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

class MembersDiffUtil(
    private val oldList: ArrayList<User> = ArrayList(),
    private val newList: ArrayList<User> = ArrayList()
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
            oldList[oldItemPosition].email != newList[newItemPosition].email -> false
            oldList[oldItemPosition].userName != newList[newItemPosition].userName -> false
            else -> true
        }
    }
}