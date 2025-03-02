package com.mfatihceliik.chatapplication.ui.customViews.receiver

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mfatihceliik.chatapplication.databinding.ReceiverProfileBinding

class ReceiverProfileView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttribute: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttribute) {

    private val binding: ReceiverProfileBinding

    init {
        binding = ReceiverProfileBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setImage(profile: Uri) {
        this.binding.userImageView.setImageURI(profile)
    }

}