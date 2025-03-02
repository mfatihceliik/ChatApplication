package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.message.ImageMessage

class ImageMessageListTypeConverter {
    @TypeConverter
    fun fromImageMessage(imageMessage: ImageMessage?): String? {
        return Gson().toJson(imageMessage)
    }

    @TypeConverter
    fun toImageMessage(json: String): ImageMessage? {
        val type = object : TypeToken<ImageMessage?>() {}.type
        return Gson().fromJson(json, type)
    }
}