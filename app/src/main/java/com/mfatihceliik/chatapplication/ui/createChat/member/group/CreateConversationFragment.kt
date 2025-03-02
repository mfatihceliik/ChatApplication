package com.mfatihceliik.chatapplication.ui.createChat.member.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfatihceliik.chatapplication.R
import com.mfatihceliik.chatapplication.SharedViewModel
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.request.CreateConversationRequest
import com.mfatihceliik.chatapplication.data.entity.enums.ViewPagerType
import com.mfatihceliik.chatapplication.databinding.CreateConversationFragmentBinding
import com.mfatihceliik.chatapplication.ui.createChat.member.MembersImageAdapter
import com.mfatihceliik.chatapplication.ui.createChat.member.MembersViewModel
import com.mfatihceliik.chatapplication.util.collectLifecycleStateStarted
import kotlinx.coroutines.launch

class CreateConversationFragment: Fragment() {

    private lateinit var binding: CreateConversationFragmentBinding
    private val membersImageAdapter = MembersImageAdapter()
    private val viewModel: MembersViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        const val TAG = "CreateGroupFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateConversationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        Log.v(TAG, "${findNavController().currentBackStack.value}")
    }

    private fun initialize() {
        setRecyclerView()
        adapterOperations()
        onClicks()
        groupNameTextChange()
        groupNameButtonClickableStatus()
        checkTickedUserListSize()
        getTickedUsers()

        shareConversation()
    }

    private fun setRecyclerView() {
        binding.membersRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.membersRecyclerView.adapter = membersImageAdapter
    }

    private fun onClicks() {
        binding.backButton.setOnClickListener {
            viewModel.viewpagerChangePage(ViewPagerType.MEMBERS_FRAGMENT.pageName)
            Log.v(TAG, findNavController().currentBackStack.value.toString())
        }

        binding.createButton.setOnClickListener {
            createChat()
            Log.v(TAG, findNavController().currentBackStack.value.toString())
        }
    }

    private fun createChat() {
        val groupName = binding.groupNameEditText.text.toString()
        val createConversationRequest = CreateConversationRequest(viewModel.getUserId(), groupName, viewModel.tickedUserStateFlow.value.toMutableList())
        viewModel.createConversation(createConversationRequest = createConversationRequest)
    }

    private fun shareConversation() {
        collectLifecycleStateStarted(viewModel.conversationResponse) { conversation ->
            sharedViewModel.setConversation(conversationResponse = conversation)
            findNavController().popBackStack()
            Log.v(TAG, findNavController().currentBackStack.value.toString())
        }
    }

    private fun adapterOperations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tickedUserRemove(membersImageAdapter = membersImageAdapter)
            }
        }
    }

    private fun groupNameButtonClickableStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.groupNameStateFlow.collect { status ->
                val tickedUsers = viewModel.checkTickedUserSizeStateFlow.value
                if(status && tickedUsers) {
                    binding.createButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_100))
                    binding.createButton.isClickable = true
                }else {
                    binding.createButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_300))
                    binding.createButton.isClickable = false
                }
            }
        }
    }

    private fun checkTickedUserListSize() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.checkTickedUserSizeStateFlow.collect { status ->
                val groupName = viewModel.groupNameStateFlow.value
                if(status && groupName) {
                    binding.createButton.isClickable = true
                    binding.createButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_100))
                }else {
                    binding.createButton.isClickable = false
                    binding.createButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_300))
                }
            }
        }
    }

    private fun groupNameTextChange() {
        binding.groupNameEditText.addTextChangedListener { editable ->
            viewModel.groupNameCreateButtonStatus(editable = editable)
        }
    }

    private fun getTickedUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tickedUserStateFlow.collect { users ->
                membersImageAdapter.setTickedUsers(tickedUserList = users)
                viewModel.tickedUserSizeCreateButtonStatus(tickedUsers = users)
            }
        }
    }
}