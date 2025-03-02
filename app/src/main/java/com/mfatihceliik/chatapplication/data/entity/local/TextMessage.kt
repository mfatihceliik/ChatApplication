package com.mfatihceliik.chatapplication.data.entity.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName = "textMessages",
    /*foreignKeys = [
        ForeignKey(
            entity = Message::class,
            parentColumns = ["message_id"],
            childColumns = ["textMessage_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]*/
)
data class TextMessage(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo("textMessage_id")
    val id: Int,
    @SerializedName("text")
    @ColumnInfo("textMessage_text")
    val text: String,
): Parcelable
