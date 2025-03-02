package com.mfatihceliik.chatapplication.util

import android.net.Uri
import android.os.Parcelable
import com.mfatihceliik.chatapplication.data.entity.enums.GalleryMediaType
import kotlinx.parcelize.Parcelize


@Parcelize
data class Content(
    val uri: Uri,
    val type: GalleryMediaType
): Parcelable
