package com.mfatihceliik.chatapplication.ui.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mfatihceliik.chatapplication.data.entity.enums.GalleryMediaType
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery
import com.mfatihceliik.chatapplication.util.onClickListeners.GallerySetOnClickListener

class GalleryAdapter: RecyclerView.Adapter<ViewHolder>() {

    private var galleryList: ArrayList<Gallery> = ArrayList()
    private var gallerySetOnClickListener: GallerySetOnClickListener? = null

    fun setGalleryList(newGalleryList: ArrayList<Gallery>) {
        val diffUtil = GalleryDiffUtil(newList = newGalleryList, oldList = this.galleryList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.galleryList = newGalleryList
        diffResult.dispatchUpdatesTo(this@GalleryAdapter)
    }

    fun setGalleryOnClickListener(gallerySetOnClickListener: GallerySetOnClickListener) {
        this.gallerySetOnClickListener = gallerySetOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when(viewType) {
            GalleryMediaType.IMAGE.ordinal -> GalleryImageViewHolder.from(parent = parent)
            GalleryMediaType.VIDEO.ordinal -> GalleryVideoViewHolder.from(parent = parent)
            else -> GalleryImageViewHolder.from(parent = parent)
        }

    override fun getItemCount(): Int = this.galleryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gallery = galleryList[position]
        when(holder.itemViewType) {
            GalleryMediaType.IMAGE.ordinal -> {
                val galleryImageViewHolder = holder as GalleryImageViewHolder
                galleryImageViewHolder.bindGalleryImage(gallery = gallery, gallerySetOnClickListener = this.gallerySetOnClickListener)
            }
            GalleryMediaType.VIDEO.ordinal -> {
                val galleryVideoViewHolder = holder as GalleryVideoViewHolder
                galleryVideoViewHolder.bindGalleryVideo(gallery = gallery, gallerySetOnClickListener = this.gallerySetOnClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val uri = galleryList[position].type
        return when(uri) {
            GalleryMediaType.IMAGE -> GalleryMediaType.IMAGE.ordinal
            GalleryMediaType.VIDEO -> GalleryMediaType.VIDEO.ordinal
        }
    }
}