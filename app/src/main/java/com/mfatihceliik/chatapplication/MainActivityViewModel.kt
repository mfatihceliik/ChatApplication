package com.mfatihceliik.chatapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val socketRepository: ChatApiSocketRepository
): ViewModel() {

    companion object {
        const val TAG = "MainActivityViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "onCleared")
    }

    init {
        //connectionError()
        //connectionSuccess()
    }

    private val _connectionStatus = MutableStateFlow(false)
    val connectionStatus: StateFlow<Boolean> = _connectionStatus.asStateFlow()

    private fun connectionError() = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " connectionError")
        socketRepository.connectionError().collectLatest { connectionStatus ->
            withContext(Dispatchers.Main) {
                Log.v(TAG, Thread.currentThread().name + " connectionError")
                _connectionStatus.emit(connectionStatus)
            }
        }
    }

    private fun connectionSuccess() = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, Thread.currentThread().name + " connectionSuccess")
        socketRepository.connectionSuccess().collect { connectionStatus ->
            withContext(Dispatchers.Main) {
                Log.v(TAG, Thread.currentThread().name + " connectionSuccess")
                _connectionStatus.emit(connectionStatus)
            }
        }
    }

    fun reConnect() {
        socketRepository.reConnect()
    }

    fun disconnect() {
        socketRepository.disconnect()
    }

}