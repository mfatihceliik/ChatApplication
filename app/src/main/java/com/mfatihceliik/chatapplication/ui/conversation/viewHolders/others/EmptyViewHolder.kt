package com.mfatihceliik.chatapplication.ui.conversation.viewHolders.others

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.databinding.EmptyConversationItemBinding

class EmptyViewHolder(val binding: EmptyConversationItemBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val TAG = "EmptyViewHolder"
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = EmptyConversationItemBinding.inflate(layoutInflater, parent, false)
            return EmptyViewHolder(binding = binding)
        }
    }

    fun bindEmptyViewHolder() {

    }

}