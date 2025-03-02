package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.message.MessageContentType

class MessageContentTypeConverter {
    @TypeConverter
    fun fromMessageContentType(messageContentType: MessageContentType): String {
        return Gson().toJson(messageContentType)
    }

    @TypeConverter
    fun toMessageContentType(json: String): MessageContentType {
        val type = object : TypeToken<MessageContentType>() {}.type
        return Gson().fromJson(json, type)
    }
}