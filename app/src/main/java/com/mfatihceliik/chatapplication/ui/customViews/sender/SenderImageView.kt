package com.mfatihceliik.chatapplication.ui.customViews.sender

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mfatihceliik.chatapplication.databinding.SenderImageMessageBinding

class SenderImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttribute) {

    private val binding: SenderImageMessageBinding

    init {
        binding = SenderImageMessageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun imageContentImageView() = binding.imageContentImageView
    fun progressBar() = binding.progressBar

    fun setImage(url: String?) {

    }

    fun setMessage(text: String?) {
        if(!text.isNullOrEmpty())
            binding.message.text = text
        else
            binding.message.visibility = GONE
    }

    fun setSendTime(time: String) {
        binding.sendTime.text = time
    }

    fun setProgressBarVisibility(visibility: Boolean?) {
        if(visibility == true)
            binding.progressBar.visibility = VISIBLE
        else
            binding.progressBar.visibility = GONE
    }

    fun setDoubleTickVisibility(visibility: Boolean?) {
        if(visibility == true)
            binding.doubleTick.visibility = VISIBLE
        else
            binding.doubleTick.visibility = INVISIBLE
    }
}