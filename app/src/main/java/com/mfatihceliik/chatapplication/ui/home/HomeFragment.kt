package com.mfatihceliik.chatapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfatihceliik.chatapplication.SharedViewModel
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.databinding.HomeFragmentBinding
import com.mfatihceliik.chatapplication.util.collectLifecycleStateStarted
import com.mfatihceliik.chatapplication.util.getQueryTextChangeStateFlow
import com.mfatihceliik.chatapplication.util.gone
import com.mfatihceliik.chatapplication.util.onClickListeners.HomeFragmentToConversationOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val conversationAdapter: ConversationAdapter = ConversationAdapter()

    companion object {
        const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        setRecyclerView()
        onClicks()
        homeFragmentToConversationDetailFragment()
        observeCreateConversation()
        joinConversations()

        //onConversations()
        getConversations()
        searchQueryChange()
        searchConversations()

        lifecycleScope.launch {
            /*val users = listOf<User>(
                User(id = 1, userName = "mfatihceliik", email = "mfatihceliik@outlook.com"),
                User(id = 2, userName = "riplaze", email = "riplaze@outlook.com"),
                User(id = 3, userName = "voltax", email = "voltax@outlook.com"),
                User(id = 4, userName = "gullu", email = "gullu@outlook.com"),
                User(id = 5, userName = "astor", email = "astor@outlook.com"),
                User(id = 6, userName = "sasa", email = "sasa@outlook.com"),
                User(id = 7, userName = "eregli", email = "eregli@outlook.com")
            )
            viewModel.insertUsers(users = users)
            val participants = listOf<Participant>(
                Participant(id = 1, userId = 1, conversationId = 1),
                Participant(id = 2, userId = 2, conversationId = 1),
                Participant(id = 3, userId = 1, conversationId = 2),
                Participant(id = 4, userId = 3, conversationId = 2),
            )
            viewModel.insertParticipants(participant = participants)
            val messageContentTypes = listOf<MessageContentType>(
                MessageContentType(id = 1, type = "Text Message"),
                MessageContentType(id = 2, type = "Image Message"),
                MessageContentType(id = 3, type = "File Message")
            )
            messageContentTypes.forEach {
                viewModel.insertMessageContentType(messageContentType = it)
            }

            viewModel.onConversations()*/

            /*viewModel.getConversationsFromDb().collect { conversationResponses ->
                val gson = Gson().toJson(conversationResponses)
                Log.v(TAG, gson.toString())
            }*/
        }

    }

    private fun getConversations() {
        lifecycleScope.launch {
            collectLifecycleStateStarted(viewModel.conversation) { conversations ->
                binding.progressBar.gone()
                if(!conversations.isNullOrEmpty()) {
                    binding.emptyPage.visibility = View.INVISIBLE
                    binding.recyclerView.visibility = View.VISIBLE
                    conversationAdapter.setConversationList(conversationResponses = conversations)
                }else {
                    binding.emptyPage.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = conversationAdapter
        }
    }

    private fun onClicks() {
        binding.newChatButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNewChatBottomSheetDialog()
            findNavController().navigate(action)
        }
    }

    private fun homeFragmentToConversationDetailFragment() {
        conversationAdapter.setHomeFragmentToConversationOnClickListener(object: HomeFragmentToConversationOnClickListener {
            override fun onClick(conversationResponse: ConversationResponse) {
                val action = HomeFragmentDirections.actionHomeFragmentToChatFragment(conversationResponse)
                this@HomeFragment.findNavController().navigate(action)
            }
        })
    }

    private fun onConversations() {
        collectLifecycleStateStarted(viewModel.combineLocalAndSocketConversation) { conversations ->
            binding.progressBar.gone()
            if(conversations.isNotEmpty()) {
                //Log.v(TAG, conversations.toString())
                binding.emptyPage.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                //conversationAdapter.setConversationList(conversationResponses = conversations)
            }else {
                binding.emptyPage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
            }
        }
    }

    private fun joinConversations() {
        collectLifecycleStateStarted(sharedViewModel.joinConversation) { participants ->
            Log.v(TAG, participants.toString())
        }
    }

    private fun observeCreateConversation() {
        sharedViewModel.conversationResponse.observe(viewLifecycleOwner) { conversation ->
            //viewModel.insertConversation(conversationResponse = conversation)
        }
    }

    private fun searchQueryChange() {
        collectLifecycleStateStarted(binding.searchView.getQueryTextChangeStateFlow()) { query ->
            //viewModel.searchConversations(searchText = query)
        }
    }

    private fun searchConversations() {
        collectLifecycleStateStarted(viewModel.searchConversations) { conversations ->
            if(!conversations.isNullOrEmpty()) {
                conversationAdapter.setConversationList(conversationResponses = conversations)
            }
        }
    }
}