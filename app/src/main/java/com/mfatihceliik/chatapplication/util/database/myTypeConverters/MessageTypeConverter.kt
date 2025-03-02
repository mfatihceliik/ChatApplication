package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message

class MessageTypeConverter {
    @TypeConverter
    fun fromMessage(message: List<Message>): String {
        return Gson().toJson(message)
    }

    @TypeConverter
    fun toMessage(json: String): List<Message> {
        val type = object : TypeToken<List<Message>>() {}.type
        return Gson().fromJson(json, type)
    }
}