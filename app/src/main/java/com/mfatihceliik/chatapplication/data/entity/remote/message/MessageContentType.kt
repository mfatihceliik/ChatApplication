package com.mfatihceliik.chatapplication.data.entity.remote.message

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "messageContentType",
    /*foreignKeys = [
        ForeignKey(
            entity = Message::class,
            parentColumns = ["message_conversationId"],
            childColumns = ["conversation_conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]*/
)
data class MessageContentType(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo("messageContentType_id")
    val id: Int,
    @SerializedName("type")
    @ColumnInfo("messageContentType_type")
    val type: String
): Parcelable
