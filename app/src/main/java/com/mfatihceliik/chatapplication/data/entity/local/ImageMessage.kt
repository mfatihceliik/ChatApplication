package com.mfatihceliik.chatapplication.data.entity.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName = "imageMessages",
    /*foreignKeys = [
        ForeignKey(
            entity = Message::class,
            parentColumns = ["message_id"],
            childColumns = ["imageMessage_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]*/
)
data class ImageMessage(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("imageMessage_id")
    val id: Int,
    @ColumnInfo("imageUrl")
    val imageUrl: String,
    @ColumnInfo("imageUri")
    val imageUri: String,
    @ColumnInfo("imageBlob", typeAffinity = ColumnInfo.BLOB)
    val imageBlob: ByteArray?,
    @ColumnInfo("imageMessage_text")
    val text: String?,
): Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageMessage

        if (imageBlob != null) {
            if (other.imageBlob == null) return false
            if (!imageBlob.contentEquals(other.imageBlob)) return false
        } else if (other.imageBlob != null) return false

        return true
    }
    override fun hashCode(): Int {
        return imageBlob?.contentHashCode() ?: 0
    }
}
