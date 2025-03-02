package com.mfatihceliik.chatapplication.ui.createChat.member


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.databinding.ItemMemberBinding
import com.mfatihceliik.chatapplication.util.onClickListeners.TickedUserSetOnClickListener

class MembersAdapter: RecyclerView.Adapter<MembersAdapter.MembersViewHolder>() {

    private var usersList: ArrayList<User> = ArrayList()
    private var tickedUserList: ArrayList<User> = ArrayList()
    private var tickedUserSetOnClickListener: TickedUserSetOnClickListener? = null
    private val TAG = "MembersAdapter"

    inner class MembersViewHolder(private val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, tickedUserSetOnClickListener: TickedUserSetOnClickListener?) {
            binding.memberUserName.text = user.userName
            binding.conversationCardView.setOnClickListener {
                tickedUserSetOnClickListener?.onClick(user = user)
            }
            changeTickedUsersVisibility(
                user = user,
                tickedUserList = tickedUserList,
                binding = binding
            )
        }

        private fun changeTickedUsersVisibility(user: User, tickedUserList: List<User>, binding: ItemMemberBinding) {
            if(tickedUserList.contains(user)) {
                binding.tickView.visibility = View.VISIBLE
            }else {
                binding.tickView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MembersViewHolder(binding)
    }

    override fun getItemCount(): Int = this.usersList.size

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val user = this.usersList[position]
        holder.bind(
            user = user,
            tickedUserSetOnClickListener = this.tickedUserSetOnClickListener
        )
    }

    fun setTicketUserSetOnClickListener(tickedUserSetOnClickListener: TickedUserSetOnClickListener) {
        this.tickedUserSetOnClickListener = tickedUserSetOnClickListener
    }

    fun setTickedUsers(tickedUserList: List<User>) {
        this.tickedUserList.clear()
        this.tickedUserList.addAll(tickedUserList)
        notifyItemRangeChanged(0, itemCount, this.tickedUserList)
    }

    fun setUsersList(usersList: List<User>) {
        val diffUtil = MembersDiffUtil(oldList = this.usersList, newList = ArrayList(usersList))
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        this.usersList = ArrayList(usersList)
        diffResults.dispatchUpdatesTo(this@MembersAdapter)
    }
}