package com.mfatihceliik.chatapplication.ui.createChat.member

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.request.CreateConversationRequest
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.util.Resource
import com.mfatihceliik.chatapplication.util.onClickListeners.TickedUserRemoveSetOnClickListener
import com.mfatihceliik.chatapplication.util.onClickListeners.TickedUserSetOnClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MembersViewModel @Inject constructor(
    private val chatApiRepository: ChatApiRepository,
    private val socketRepository: ChatApiSocketRepository,
): ViewModel() {

    companion object {
        const val TAG = "MembersViewModel"
    }

    init {
        getAllUsers()
    }

    fun getUserId() = chatApiRepository.getUser().id

    private val _tickedUserStateFlow = MutableStateFlow(emptyList<User>())
    val tickedUserStateFlow: StateFlow<List<User>> = _tickedUserStateFlow.asStateFlow()

    private val _usersStateFlow = MutableStateFlow(emptyList<User>())
    val usersStateFlow: StateFlow<List<User>> = _usersStateFlow.asStateFlow()

    private val _viewpagerChangePage = MutableSharedFlow<String>()
    val viewpagerChangePage: SharedFlow<String> = _viewpagerChangePage.asSharedFlow()

    private val _groupNameStateFlow = MutableStateFlow(false)
    val groupNameStateFlow: StateFlow<Boolean> = _groupNameStateFlow.asStateFlow()

    private val _checkTickedUserSizeStateFlow = MutableStateFlow(false)
    val checkTickedUserSizeStateFlow: StateFlow<Boolean> = _checkTickedUserSizeStateFlow.asStateFlow()

    private val _conversationResponse = MutableSharedFlow<ConversationResponse>()
    val conversationResponse: SharedFlow<ConversationResponse> = _conversationResponse.asSharedFlow()

    fun createConversation(createConversationRequest: CreateConversationRequest) = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " createConversation")
        socketRepository.createConversation(createConversationRequest = createConversationRequest)
            .flowOn(Dispatchers.IO).collect {
                when(it.status) {
                    Resource.Status.LOADING -> Log.v(TAG, " LOADING createConversation")
                    Resource.Status.SUCCESS -> {
                        it.data?.let { conversation ->
                            Log.v(TAG, "SUCCESS createConversation")
                            _conversationResponse.emit(conversation)
                        }
                    }
                    Resource.Status.ERROR -> Log.v(TAG, " ERROR createConversation")
                }
            }
    }


    fun viewpagerChangePage(page: String) {
        viewModelScope.launch {
            Log.v(TAG, Thread.currentThread().name + " viewpagerChangePage")
            _viewpagerChangePage.emit(page)
        }
    }

    fun tickedUserSizeCreateButtonStatus(tickedUsers: List<User>) {
        viewModelScope.launch {
            Log.v(TAG, Thread.currentThread().name + " tickedUserSizeCreateButtonStatus")
            if(tickedUsers.isNotEmpty())
                _checkTickedUserSizeStateFlow.emit(true)
            else
                _checkTickedUserSizeStateFlow.emit(false)
        }
    }

    fun groupNameCreateButtonStatus(editable: Editable?) {
        viewModelScope.launch {
            Log.v(TAG, Thread.currentThread().name + " groupNameCreateButtonStatus")
            editable?.let { if(it.length >= 3) _groupNameStateFlow.emit(true) else _groupNameStateFlow.emit(false) }
        }
    }

    fun clearTickedUsers() {
        viewModelScope.launch {
            Log.v(TAG, Thread.currentThread().name + " clearTickedUsers")
            _tickedUserStateFlow.emit(emptyList())
        }
    }

    private fun getAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " getAllUsers")
        socketRepository.getAllUsers(getUserId()).collect {
            when(it.status) {
                Resource.Status.LOADING -> Log.v(TAG, "getAllUsers LOADING")
                Resource.Status.SUCCESS -> it.data?.let { users ->
                    withContext(Dispatchers.Main) {
                        Log.v(TAG, Thread.currentThread().name + " getAllUsers")
                        _usersStateFlow.emit(users)
                    }
                }
                Resource.Status.ERROR -> Log.v(TAG, "getAllUsers ERROR")
            }
        }
    }

    suspend fun tickedUser(membersAdapter: MembersAdapter) {
        membersAdapter.setTicketUserSetOnClickListener(object: TickedUserSetOnClickListener {
            override fun onClick(user: User) {
                viewModelScope.launch {
                    val currentList = _tickedUserStateFlow.value.toMutableList()
                    withContext(Dispatchers.Default) {
                        Log.v(TAG, Thread.currentThread().name + " tickedUser")
                        if(currentList.contains(user)){
                            currentList.remove(user)
                        }else {
                            currentList.add(user)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        Log.v(TAG, Thread.currentThread().name + " tickedUser")
                        _tickedUserStateFlow.value = currentList
                    }
                }
            }
        })
    }

    suspend fun tickedUserRemove(membersImageAdapter: MembersImageAdapter) {
        membersImageAdapter.setTickedUserRemoveSetOnClickListener(object: TickedUserRemoveSetOnClickListener {
            override fun onClick(user: User) {
                viewModelScope.launch {
                    val currentList = _tickedUserStateFlow.value.toMutableList()
                    withContext(Dispatchers.Default) {
                        Log.v(TAG, Thread.currentThread().name + " tickedUserRemove")
                        if (currentList.contains(user)) {
                            currentList.remove(user)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        Log.v(TAG, Thread.currentThread().name + " tickedUserRemove")
                        _tickedUserStateFlow.value = currentList
                    }
                }
            }
        })
    }
}