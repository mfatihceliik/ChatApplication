package com.mfatihceliik.chatapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

class SharedPreferenceManager(context: Context) {
    companion object {
        const val TOKEN: String = "com.mfatihceliik.chatapplication.TOKEN"
        const val USER: String = "com.mfatihceliik.chatapplication.USER"
    }

    private val sharedPreference: SharedPreferences = context.getSharedPreferences("sharedPreferencesUtil", Context.MODE_PRIVATE)

    fun saveToken(token: String?) {
        sharedPreference.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreference.getString(TOKEN, "")
    }

    fun saveUser(user: User?) {
        val json = Gson().toJson(user)
        sharedPreference.edit().putString(USER, json).apply()
    }

    fun getUser(): User? {
        val user = sharedPreference.getString(USER, "")
        return Gson().fromJson(user, User::class.java)
    }
}