package com.mfatihceliik.chatapplication.ui.customViews.sender

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.mfatihceliik.chatapplication.databinding.ReceiverProfileBinding

class SenderProfileView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttribute) {

    private val binding: ReceiverProfileBinding

    init {
        binding = ReceiverProfileBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setImage(url: String?) {
        Glide.with(binding.userImageView.context).load(url).into(binding.userImageView)
    }

    fun setProfileImageVisibility(visibility: Boolean) {
        if (visibility == true)
            binding.userImageView.visibility = VISIBLE
        else
            binding.userImageView.visibility = GONE
    }

}