package com.mfatihceliik.chatapplication.data.entity.remote.message

import android.os.Parcelable
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.mfatihceliik.chatapplication.util.database.myTypeConverters.DateTypeConverter
import com.mfatihceliik.chatapplication.util.database.myTypeConverters.UserTypeConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@TypeConverters(
    UserTypeConverter::class,
    DateTypeConverter::class
)
data class ImageMessage(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("imageUri")
    val imageUri: String? = null,
    @SerializedName("imageBlob")
    val imageBlob: ByteArray? = null,
    @SerializedName("text")
    val text: String? = null,
): Parcelable