package com.mfatihceliik.chatapplication.data.remote

import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginRequest
import com.mfatihceliik.chatapplication.util.BaseDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val chatApplicationApiService: ChatApplicationApiService
): BaseDataSource() {
    suspend fun login(loginRequest: LoginRequest) = getResult { chatApplicationApiService.login(loginRequest = loginRequest) }
}