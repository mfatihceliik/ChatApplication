package com.mfatihceliik.chatapplication.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mfatihceliik.chatapplication.R
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery
import com.mfatihceliik.chatapplication.databinding.ItemImageGalleryBinding
import com.mfatihceliik.chatapplication.databinding.ItemVideoGalleryBinding
import com.mfatihceliik.chatapplication.util.calculateDuration
import com.mfatihceliik.chatapplication.util.onClickListeners.GallerySetOnClickListener

class GalleryImageViewHolder(private val binding: ItemImageGalleryBinding): ViewHolder(binding.root) {

    companion object {
        private const val TAG = "GalleryImageViewHolder"
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemImageGalleryBinding.inflate(layoutInflater, parent, false)
            return GalleryImageViewHolder(binding)
        }
    }

    fun bindGalleryImage(gallery: Gallery, gallerySetOnClickListener: GallerySetOnClickListener?) {



        //Glide.with(binding.root.context).load(gallery.uri).into(binding.imageView)
        loadImage(gallery = gallery)
        binding.imageView.setOnClickListener {
            gallerySetOnClickListener?.onClick(gallery)
        }
    }

    private fun loadImage(gallery: Gallery) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.preview_image)
            .error(R.drawable.error_image)
            .priority(Priority.HIGH)
            .override(Target.SIZE_ORIGINAL)
        GlideImageLoader(
            imageView = binding.imageView,
            progressBar = binding.progressBar
        ).load(
            url = gallery.uri.toString(),
            requestOptions = requestOptions
        )
    }
}

class GalleryVideoViewHolder(private val binding: ItemVideoGalleryBinding): ViewHolder(binding.root) {

    companion object {
        private const val TAG = "GalleryVideoViewHolder"
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemVideoGalleryBinding.inflate(layoutInflater, parent, false)
            return GalleryVideoViewHolder(binding)
        }
    }

    fun bindGalleryVideo(gallery: Gallery, gallerySetOnClickListener: GallerySetOnClickListener?) {
        Glide.with(binding.root.context).load(gallery.uri).into(binding.imageView)
        binding.duration.text = calculateDuration(gallery.duration!!)
        binding.imageView.setOnClickListener {
            gallerySetOnClickListener?.onClick(gallery)
        }
    }

}