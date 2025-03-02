package com.mfatihceliik.chatapplication.data.entity.local.gallery

import android.net.Uri
import android.os.Parcelable
import com.mfatihceliik.chatapplication.data.entity.enums.GalleryMediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gallery(
    val uri: Uri,
    val dateModified: Long,
    val duration: Long?,
    val type: GalleryMediaType
): Parcelable
