package com.mfatihceliik.chatapplication.ui.createChat.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfatihceliik.chatapplication.R
import com.mfatihceliik.chatapplication.SharedViewModel
import com.mfatihceliik.chatapplication.data.entity.enums.ViewPagerType
import com.mfatihceliik.chatapplication.databinding.MembersFragmentBinding
import com.mfatihceliik.chatapplication.util.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MembersFragment: Fragment() {

    private lateinit var binding: MembersFragmentBinding
    private val viewModel: MembersViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val membersAdapter = MembersAdapter()
    private val membersImageAdapter = MembersImageAdapter()

    companion object {
        const val TAG = "MembersFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MembersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
        Log.v(TAG, findNavController().currentBackStack.value.toString())
    }

    private fun initialize() {
        setRecyclerViews()
        getTickedUsers()
        getUsers()
        adapterOperation()
        onClicks()
        checkTickedUserListSize()

    }

    private fun setRecyclerViews() {
        binding.membersRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = membersAdapter
        }
        binding.membersImageRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = membersImageAdapter
        }
    }

    private fun onClicks() {
        binding.nextButton.setOnClickListener {
            viewModel.viewpagerChangePage(ViewPagerType.CREATE_CONVERSATION_FRAGMENT.pageName)
            Log.v(TAG, findNavController().currentBackStack.value.toString())
        }

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
            Log.v(TAG, findNavController().currentBackStack.value.toString())
        }
    }

    private fun checkTickedUserListSize() {
        collectLatestLifecycleFlow(viewModel.checkTickedUserSizeStateFlow) { status ->
            if(status) {
                binding.nextButton.isClickable = true
                binding.nextButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_100))
            }else {
                binding.nextButton.isClickable = false
                binding.nextButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_300))
            }
        }
    }

    private fun adapterOperation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tickedUser(membersAdapter = membersAdapter)
                viewModel.tickedUserRemove(membersImageAdapter = membersImageAdapter)
            }
        }
    }

    private fun getUsers() {
        collectLatestLifecycleFlow(viewModel.usersStateFlow) { users ->
            membersAdapter.setUsersList(usersList = users)
        }
    }

    private fun getTickedUsers() {
        collectLatestLifecycleFlow(viewModel.tickedUserStateFlow) { users ->
            membersAdapter.setTickedUsers(tickedUserList = users)
            membersImageAdapter.setTickedUsers(tickedUserList = users)
            viewModel.tickedUserSizeCreateButtonStatus(tickedUsers = users)
        }
    }
}