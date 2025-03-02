package com.mfatihceliik.chatapplication.data.entity.remote.participant

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "participants",
    primaryKeys = ["participant_userId", "participant_conversationId"]
    /*foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["participant_userId"]
        ),
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["conversation_id"],
            childColumns = ["participant_conversationId"]
        )
    ]*/
)
data class Participant(
    @SerializedName("id")
    @ColumnInfo("participant_id")
    val id: Int,
    @SerializedName("userId")
    @ColumnInfo("participant_userId", index = true)
    val userId: Int,
    @SerializedName("conversationId")
    @ColumnInfo("participant_conversationId", index = true)
    val conversationId: Int
): Parcelable
