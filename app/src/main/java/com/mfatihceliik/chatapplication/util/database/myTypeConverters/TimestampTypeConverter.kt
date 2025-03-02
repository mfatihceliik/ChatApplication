package com.mfatihceliik.chatapplication.util.database.myTypeConverters

import androidx.room.TypeConverter
import java.sql.Timestamp

class TimestampTypeConverter {

    @TypeConverter
    fun fromLong(value: Long?): Timestamp? {
        return if (value == null) null else Timestamp(value)
    }

    @TypeConverter
    fun toLong(value: Timestamp?): Long? {
        return value?.time
    }
}