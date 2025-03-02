package com.mfatihceliik.chatapplication.ui.gallery

import androidx.recyclerview.widget.DiffUtil
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery

class GalleryDiffUtil(
    private val newList: ArrayList<Gallery>,
    private val oldList: ArrayList<Gallery>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].uri == newList[newItemPosition].uri
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].uri != newList[newItemPosition].uri -> false
            oldList[oldItemPosition].type != newList[newItemPosition].type -> false
            oldList[oldItemPosition].duration != newList[newItemPosition].duration -> false
            oldList[oldItemPosition].dateModified != newList[newItemPosition].dateModified -> false
            else -> true
        }
    }
}