package com.mfatihceliik.chatapplication.data.remote

import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginRequest
import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApplicationApiService {

    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}