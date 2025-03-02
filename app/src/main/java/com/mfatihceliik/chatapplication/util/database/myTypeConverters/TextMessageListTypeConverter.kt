package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.message.TextMessage

class TextMessageListTypeConverter {
    @TypeConverter
    fun fromTextMessages(textMessages: List<TextMessage>?): String? {
        return Gson().toJson(textMessages)
    }

    @TypeConverter
    fun toTextMessages(json: String): List<TextMessage>? {
        val type = object : TypeToken<List<TextMessage>?>() {}.type
        return Gson().fromJson(json, type)
    }
}