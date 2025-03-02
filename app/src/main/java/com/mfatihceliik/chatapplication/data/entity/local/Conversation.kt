package com.mfatihceliik.chatapplication.data.entity.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mfatihceliik.chatapplication.util.database.myTypeConverters.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.util.Date

@TypeConverters(
    DateTypeConverter::class
)
@Parcelize
@Entity("conversations")
data class Conversation(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("conversation_id")
    val id: Int,
    @ColumnInfo("conversation_creatorId")
    val creatorId: Int,
    @ColumnInfo("conversation_name")
    val conversationName: String,
    @ColumnInfo("conversation_createdDate")
    val createdDate: Date,
): Parcelable
