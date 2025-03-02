package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message

class MessageListTypeConverter {
    @TypeConverter
    fun fromTextMessageList(messages: List<Message>): String {
        return Gson().toJson(messages)
    }

    @TypeConverter
    fun toTextMessageList(json: String): List<Message> {
        val type = object : TypeToken<List<Message>>() {}.type
        return Gson().fromJson(json, type)
    }
}