package com.mfatihceliik.chatapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginRequest
import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginResponse
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val chatApiRepository: ChatApiRepository
): ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
        const val EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
    }

    private val _emailEditText = MutableStateFlow<String>("empty")
    val emailEditText = _emailEditText.asStateFlow()

    private val _isValidEmailAddressStateFlow = MutableStateFlow<Boolean>(false)
    val isValidEmailAddressStateFlow = _isValidEmailAddressStateFlow.asStateFlow()

    private val _passwordEyeState = MutableStateFlow<Boolean>(true)
    val passwordEyeShowStateFlow = _passwordEyeState.asStateFlow()



    private suspend fun isValidEmailAddress() {
        emailEditText.collect {
            val patternCompile = Pattern.compile(EMAIL_ADDRESS_PATTERN)
            val matcher = patternCompile.matcher(it)
            _isValidEmailAddressStateFlow.value = matcher.matches()
        }
    }

    fun changePasswordEyeState(value: Boolean) {
        _passwordEyeState.value = value
    }

    fun changeEmailTextState(value: String) {
        _emailEditText.value = value
        viewModelScope.launch {
            isValidEmailAddress()
        }
    }

    fun login(loginRequest: LoginRequest): LiveData<Resource<LoginResponse>> =
        chatApiRepository.login(loginRequest = loginRequest)

    fun saveUser(user: User?) {
        chatApiRepository.saveUser(user = user)
    }
}