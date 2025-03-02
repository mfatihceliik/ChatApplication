package com.mfatihceliik.chatapplication.ui.gallery

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.mfatihceliik.chatapplication.data.entity.enums.GalleryMediaType
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery

class ImagesGallery {

    companion object {
        fun getGalleryDataAsUri(context: Context): ArrayList<Gallery> {
            val galleryList = ArrayList<Gallery>()

            val imageProjection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED
            )

            val videoProjection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DURATION
            )

            // Resimler için sorgu
            val imageCursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC"
            )

            imageCursor?.use { cursor ->
                val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateModifiedColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                while (cursor.moveToNext()) {
                    val imageId = cursor.getLong(idColumnIndex)
                    val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId.toString())
                    val dateModified = cursor.getLong(dateModifiedColumnIndex)
                    galleryList.add(Gallery(imageUri, dateModified, null, GalleryMediaType.IMAGE))
                }
            }

            // Videolar için sorgu
            val videoCursor = context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoProjection,
                null,
                null,
                MediaStore.Video.Media.DATE_MODIFIED + " DESC"
            )

            videoCursor?.use { cursor ->
                val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val dateModifiedColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
                val durationColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                while (cursor.moveToNext()) {
                    val videoId = cursor.getLong(idColumnIndex)
                    val videoUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId.toString())
                    val dateModified = cursor.getLong(dateModifiedColumnIndex)
                    val duration = cursor.getLong(durationColumnIndex)
                    galleryList.add(Gallery(videoUri, dateModified, duration, GalleryMediaType.VIDEO))
                }
            }

            galleryList.sortByDescending { it.dateModified }

            return galleryList
        }
    }
}
