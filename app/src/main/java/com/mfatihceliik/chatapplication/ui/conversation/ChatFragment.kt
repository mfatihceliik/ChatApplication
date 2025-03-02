package com.mfatihceliik.chatapplication.ui.conversation

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mfatihceliik.chatapplication.SharedViewModel
import com.mfatihceliik.chatapplication.databinding.FragmentChatBinding
import com.mfatihceliik.chatapplication.util.collectLifecycleStateResumed
import com.mfatihceliik.chatapplication.util.collectLifecycleStateStarted
import com.mfatihceliik.chatapplication.util.collectViewLifecycleStateStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatFragment: Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: ChatViewModel by viewModels()

    private lateinit var chatAdapter: ChatCustomViewAdapter

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    companion object {
        private const val TAG = "ChatFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted)
                accessGallery()
            else {
                Snackbar.make(requireView(), "Please grant permission to gallery.", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
    }

    private fun initialize() {
        setRecyclerViewAndAdapter()
        onClicks()
        messageEditTextChangedListener()
        sendButtonVisibility()
        userTyping()
        sharedViewModel.joinConversations()
        joinConversations()
        shareConversationId()

        // RecyclerView
        addMessageToRecyclerView()

        // Message
        observeMessage()
        callbackMessage()

        loadMessages()
    }

    private fun setRecyclerViewAndAdapter() {
        chatAdapter = ChatCustomViewAdapter(userId = viewModel.getUserId())
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = chatAdapter
            setItemViewCacheSize(20)
            hasFixedSize()
        }
        binding.conversationTitle.text = viewModel.conversationResponse.conversationName
        binding.groupMembers.text = viewModel.groupMembers(viewModel.conversationResponse.groupMembers)
    }

    private fun loadMessages() {
        val messageModels = viewModel.loadMessages()
        chatAdapter.loadMessage(messages = messageModels)
        recyclerviewScroll()
    }

    private fun onKeyboardOpened() {
        binding.chatRecyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if(bottom < oldBottom) {
                //recyclerviewScroll()
                //binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun onClicks() {
        binding.sendButton.setOnClickListener {
            sendTextMessage()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fileImageButton.setOnClickListener {
            //imageConverterUtil.openImageAndVideo()
//            val action = ChatFragmentDirections.actionChatFragmentToGalleryFragment()
//            findNavController().navigate(action)
            checkGalleryPermission()
        }

        binding.openCameraButton.setOnClickListener {

        }
    }

    private fun accessGallery() {
        val action = ChatFragmentDirections.actionChatFragmentToGalleryFragment()
        findNavController().navigate(action)
    }

    private fun checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Use the requestPermessionLauncher to request the READ_MEDIA_IMAGES permission
            requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
        } else {
            // For older Android versions, use READ_EXTERNAL_STORAGE permission
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
        }

    }

    private fun messageEditTextChangedListener() {
        binding.messageEditText.addTextChangedListener {
            viewModel.sendMessageButtonVisibilityState(it)
        }
    }

    private fun sendButtonVisibility() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sendMessageButtonVisibility.collect {
                if(it)
                    binding.sendButton.visibility = View.VISIBLE
                else
                    binding.sendButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun joinConversations() {
        collectLifecycleStateStarted(sharedViewModel.joinConversation) { participants ->
            Log.v(TAG, participants.toString())
        }
    }

    private fun userTyping() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.userTyping.collect { typing ->
                Log.v(TAG, typing.toString())
                if (typing?.isTyping == true) {
                    binding.userTyping.visibility = View.VISIBLE
                    binding.userTyping.text = typing.user?.userName + " is typing.."
                }else {
                    binding.userTyping.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun addMessageToRecyclerView() {
        collectViewLifecycleStateStarted(sharedViewModel.addMessageToRecyclerView) { message ->
            chatAdapter.insertMessageItem(message = message)
            recyclerviewScroll()
        }
    }

    private fun observeMessage() {
        collectViewLifecycleStateStarted(viewModel.onMessage) { message ->
            chatAdapter.insertMessageItem(message = message)
            recyclerviewScroll()
        }
    }

    private fun callbackMessage() {
        collectViewLifecycleStateStarted(sharedViewModel.callBackMessage) { message ->
            chatAdapter.updateMessage(newMessage = message)
        }
    }

    private fun sendTextMessage() {
        val text = binding.messageEditText.text.toString()
        val textMessage = viewModel.createTextMessageModel(text = text)
        //sharedViewModel.saveMessageToLocalDb(message = textMessage)
        sharedViewModel.addMessageToRecyclerView(message = textMessage)
        sharedViewModel.sendTextMessage(message = textMessage)
        binding.messageEditText.text.clear()
    }

    private fun shareConversationId() {
        sharedViewModel.setConversationId(viewModel.getConversationId())
    }

    private fun recyclerviewScroll() {
        binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop")
    }
}


