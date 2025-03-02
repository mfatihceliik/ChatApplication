package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.message.TextMessage

class TextMessageTypeConverter {
    @TypeConverter
    fun fromTextMessage(textMessage: TextMessage?): String? {
        return Gson().toJson(textMessage)
    }

    @TypeConverter
    fun toTextMessage(json: String?): TextMessage? {
        val type = object : TypeToken<TextMessage>() {}.type
        return Gson().fromJson(json, type)
    }
}
