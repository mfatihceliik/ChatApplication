package com.mfatihceliik.chatapplication.ui.conversation.viewHolders.sender

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mfatihceliik.chatapplication.R
import com.mfatihceliik.chatapplication.data.entity.remote.message.ImageMessage
import com.mfatihceliik.chatapplication.databinding.SenderImageMessageWithProifeImageBinding
import com.mfatihceliik.chatapplication.ui.models.MessageUiModel
import com.mfatihceliik.chatapplication.util.lastMessageTime

class SenderImageMessageViewHolder(
    private val binding: SenderImageMessageWithProifeImageBinding
): RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val TAG = "SenderImageMessageVH"

        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SenderImageMessageWithProifeImageBinding.inflate(layoutInflater, parent, false)
            return SenderImageMessageViewHolder(binding)
        }
    }

    fun bind(
        currentMessage: MessageUiModel,
        imageMessage: ImageMessage,
        previousMessage: MessageUiModel?,
        nextMessage: MessageUiModel?
    ) {

        isProfileImageShown(currentMessage = currentMessage, nextMessage = nextMessage, previousMessage = previousMessage)
        checkMessageSent(currentMessage = currentMessage)
        //loadImage(currentMessage = currentMessage)
        loadImageWithGlide(currentMessage = currentMessage, imageMessage = imageMessage)

        binding.profileView.visibility = profileImageVisibility(visibility = currentMessage.isProfileImageShown)
        binding.imageView.setDoubleTickVisibility(visibility = currentMessage.doubleTickVisibility)
        binding.imageView.setProgressBarVisibility(currentMessage.progressbarVisibility)
        binding.imageView.setMessage(currentMessage.message.imageMessage?.text)
        binding.imageView.setSendTime(time = lastMessageTime(currentMessage.message.sendAt))

        binding.imageView.setOnClickListener {
            Log.v(TAG, currentMessage.toString())
        }
    }

    private fun loadImageWithGlide(currentMessage: MessageUiModel, imageMessage: ImageMessage) {
        val requestOptions  = RequestOptions()
            .error(R.drawable.error_image)

        Glide.with(binding.root).load(imageMessage.imageUrl).apply {
            object: RequestListener<ImageView> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<ImageView>?,
                    isFirstResource: Boolean
                ): Boolean {
                    currentMessage.progressbarVisibility = false
                    binding.imageView.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: ImageView?,
                    model: Any?,
                    target: Target<ImageView>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    currentMessage.progressbarVisibility = false
                    binding.imageView.visibility = View.VISIBLE
                    return true
                }
            }
        }.transition(withCrossFade()).into(binding.imageView.imageContentImageView())

    }

    private fun profileImageVisibility(visibility: Boolean): Int =
        if(visibility)
            View.VISIBLE
        else
            View.GONE

    private fun isProfileImageShown(currentMessage: MessageUiModel, nextMessage: MessageUiModel?, previousMessage: MessageUiModel?) =
        if(currentMessage.message.user?.id == previousMessage?.message?.user?.id) {
            currentMessage.isProfileImageShown = false
            //previousMessage?.isProfileImageShown = false
        }
        else
            currentMessage.isProfileImageShown = true

    private fun checkMessageSent(currentMessage: MessageUiModel) {
        if(currentMessage.message.isSend == true) {
            currentMessage.doubleTickVisibility = true
        }else {
            currentMessage.doubleTickVisibility = false
        }
    }
}