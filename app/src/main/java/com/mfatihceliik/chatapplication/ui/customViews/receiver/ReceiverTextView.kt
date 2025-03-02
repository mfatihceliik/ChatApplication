package com.mfatihceliik.chatapplication.ui.customViews.receiver

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mfatihceliik.chatapplication.databinding.ReceiverTextMessageBinding

class ReceiverTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttribute) {

    private val binding: ReceiverTextMessageBinding

    init {
        binding = ReceiverTextMessageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setSenderNameVisibility(visibility: Boolean) {
        if (visibility)
            binding.senderName.visibility = View.VISIBLE
        else
            binding.senderName.visibility = View.GONE
    }

    fun setSenderName(name: String?) {
        name?.let {
            binding.senderName.text = name
        }
    }

    fun setMessage(text: String) {
        binding.message.text = text
    }

    fun setSendTime(time: String) {
        binding.sendTime.text = time
    }
}