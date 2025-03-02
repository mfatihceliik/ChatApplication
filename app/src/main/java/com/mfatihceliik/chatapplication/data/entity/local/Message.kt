package com.mfatihceliik.chatapplication.data.entity.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mfatihceliik.chatapplication.util.database.myTypeConverters.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@TypeConverters(
    DateTypeConverter::class
)

@Parcelize
@Entity(
    tableName = "messages",
    /*foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["conversation_id"],
            childColumns = ["message_conversationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MessageContentType::class,
            parentColumns = ["messageContentType_id"],
            childColumns = ["message_messageContentTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]*/
)
data class Message(
    //@PrimaryKey(autoGenerate = false)
    @ColumnInfo("message_id", index = true)
    val id: Int,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("message_uuId")
    val uuId: String = UUID.randomUUID().toString() + System.currentTimeMillis().toString(),
    @ColumnInfo("message_conversationId", index = true)
    val conversationId: Int,
    @ColumnInfo("message_userId", index = true)
    val userId: Int,
    @ColumnInfo("message_messageContentTypeId", index = true)
    val messageContentTypeId: Int,
    @ColumnInfo("message_isSend")
    val isSend: Boolean,
    @ColumnInfo("message_sendAt")
    val sendAt: Date
): Parcelable
