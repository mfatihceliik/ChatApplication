package com.mfatihceliik.chatapplication.data.entity.remote.user

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("users")
data class User(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo("user_id")
    val id: Int,
    @SerializedName("userName")
    @ColumnInfo("user_userName")
    val userName: String,
    @SerializedName("email")
    @ColumnInfo("user_email")
    val email: String,
    @SerializedName("token")
    @ColumnInfo("user_token")
    val token: String,
): Parcelable
