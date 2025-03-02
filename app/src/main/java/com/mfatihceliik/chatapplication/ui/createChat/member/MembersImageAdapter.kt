package com.mfatihceliik.chatapplication.ui.createChat.member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.databinding.ItemMemberImageBinding
import com.mfatihceliik.chatapplication.util.onClickListeners.TickedUserRemoveSetOnClickListener

class MembersImageAdapter: RecyclerView.Adapter<MembersImageAdapter.MembersImageViewHolder>() {

    private var tickedUserList: ArrayList<User> = ArrayList()
    private var tickedUserRemoveSetOnClickListener: TickedUserRemoveSetOnClickListener? = null

    inner class MembersImageViewHolder(private val binding: ItemMemberImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, tickedUserRemoveSetOnClickListener: TickedUserRemoveSetOnClickListener?) {
            binding.userName.text = user.userName
            binding.remove.setOnClickListener {
                tickedUserRemoveSetOnClickListener?.onClick(user = user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersImageViewHolder {
        val binding = ItemMemberImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MembersImageViewHolder(binding)
    }

    override fun getItemCount(): Int = tickedUserList.size

    override fun onBindViewHolder(holder: MembersImageViewHolder, position: Int) {
        val user = this.tickedUserList[position]
        holder.bind(user, this.tickedUserRemoveSetOnClickListener)
    }

    fun setTickedUsers(tickedUserList: List<User>) {
        val diffUtil = MembersDiffUtil(oldList = this.tickedUserList, newList = ArrayList(tickedUserList))
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.tickedUserList = ArrayList(tickedUserList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setTickedUserRemoveSetOnClickListener(tickedUserRemoveSetOnClickListener: TickedUserRemoveSetOnClickListener) {
        this.tickedUserRemoveSetOnClickListener = tickedUserRemoveSetOnClickListener
    }

}