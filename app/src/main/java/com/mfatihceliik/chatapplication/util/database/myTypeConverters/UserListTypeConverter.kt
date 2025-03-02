package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

class UserListTypeConverter {
    @TypeConverter
    fun fromList(userList: List<User>?): String {
        val gson = Gson()
        return gson.toJson(userList)
    }

    @TypeConverter
    fun toList(userListString: String): List<User>? {
        val gson = Gson()
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(userListString, type)
    }
}