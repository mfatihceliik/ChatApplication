package com.mfatihceliik.chatapplication.ui.customViews.receiver

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mfatihceliik.chatapplication.databinding.ReceiverImageMessageBinding

class ReceiverImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttribute) {

    private val binding: ReceiverImageMessageBinding

    init {
        binding = ReceiverImageMessageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setImageView(drawable: Drawable) {
        binding.imageContentImageView.setImageDrawable(drawable)
    }

    fun setMessage(text: String) {
        binding.message.text = text
    }

    fun setSendTime(time: String) {
        binding.sendTime.text = time
    }

}