package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

class UserTypeConverter {
    @TypeConverter
    fun fromList(user: User): String {
        val gson = Gson()
        return gson.toJson(user)
    }

    @TypeConverter
    fun toList(userString: String): User {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson(userString, type)
    }
}