package com.mfatihceliik.chatapplication.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.util.onClickListeners.HomeFragmentToConversationOnClickListener
import kotlin.collections.ArrayList

class ConversationAdapter(

): RecyclerView.Adapter<ConversationViewHolder>() {

    companion object {
        const val TAG = "ConversationAdapter"
    }

    private var homeFragmentToConversationOnClickListener: HomeFragmentToConversationOnClickListener? = null
    private var conversationResponseList: ArrayList<ConversationResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder = ConversationViewHolder.from(parent = parent)
    override fun getItemCount(): Int = conversationResponseList.size

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversations = this.conversationResponseList[position]
        val message = conversations.messages?.lastOrNull()

        holder.bind(
            conversationResponse = conversations,
            message = message,
            homeFragmentToConversationOnClickListener = this.homeFragmentToConversationOnClickListener,
        )
    }

    fun setHomeFragmentToConversationOnClickListener(homeFragmentToConversationOnClickListener: HomeFragmentToConversationOnClickListener?) {
        this.homeFragmentToConversationOnClickListener = homeFragmentToConversationOnClickListener
    }

    fun setConversationList(conversationResponses: List<ConversationResponse>) {
        val diffUtil = ConversationDiffUtil(oldList = this.conversationResponseList, newList =  ArrayList(conversationResponses))
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        this.conversationResponseList = ArrayList(conversationResponses)
        diffResults.dispatchUpdatesTo(this@ConversationAdapter)
    }
}