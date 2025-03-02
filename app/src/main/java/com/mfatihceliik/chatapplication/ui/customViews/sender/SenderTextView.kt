package com.mfatihceliik.chatapplication.ui.customViews.sender

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mfatihceliik.chatapplication.databinding.SenderTextMessageBinding

class SenderTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttribute) {

    private val binding: SenderTextMessageBinding // = SenderTextMessageBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        binding = SenderTextMessageBinding.inflate(LayoutInflater.from(context), this, true)
        /*this.let {
            background = ContextCompat.getDrawable(context, R.drawable.speech_bubble_outgoing)
            val padding = resources.getDimension(R.dimen.message_item_space_8dp).toInt()
            setPadding(padding, padding, padding, padding)
        }

        binding.message.let {
            it.textSize = resources.getDimension(R.dimen.text_message_text_size_14sp)
            it.setTextColor(ContextCompat.getColor(context, R.color.white_100))
            it.maxWidth = resources.getDimension(R.dimen.text_message_max_width_200dp).toInt()
        }

        binding.sendTime.let {
            it.setTextColor(ContextCompat.getColor(context, R.color.white_100))
            it.textSize = resources.getDimension(R.dimen.text_message_time_11sp)
        }

        binding.progressBar.let {
            it.width = 10F
            it.height = 10F
            it.visibility = View.VISIBLE
        }*/
    }

    fun setMessageText(text: String?) {
        binding.message.text = text
    }

    fun setSendTime(text: String?) {
        binding.sendTime.text = text
    }

    fun setProgressBarVisibility(visibility: Boolean?) {
        if(visibility == false)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    fun setDoubleTickVisibility(visibility: Boolean?) {
        if(visibility == true) {
            binding.doubleTick.visibility = View.VISIBLE
        }else {
            binding.doubleTick.visibility = View.INVISIBLE
        }

    }

}