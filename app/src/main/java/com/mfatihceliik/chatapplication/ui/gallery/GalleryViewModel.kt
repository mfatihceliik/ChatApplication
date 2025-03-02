package com.mfatihceliik.chatapplication.ui.gallery

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfatihceliik.chatapplication.data.ChatApiRepository
import com.mfatihceliik.chatapplication.data.ChatApiSocketRepository
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val chatApiRepository: ChatApiRepository,
    private val socketRepository: ChatApiSocketRepository,
    @ApplicationContext private val context: Context,
): ViewModel() {

    companion object {
        const val TAG = "GalleryViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "onCleared")
    }


    private val _conversationId = MutableLiveData<Int>()
    val conversationId: MutableLiveData<Int> = _conversationId

    private val _gallerySharedFlow = MutableSharedFlow<ArrayList<Gallery>>(1)
    val gallerySharedFlow: SharedFlow<ArrayList<Gallery>> = _gallerySharedFlow.asSharedFlow()
    
    fun launchGallery() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.v(TAG, Thread.currentThread().name + " launchGallery")
            _gallerySharedFlow.emit(ImagesGallery.getGalleryDataAsUri(context = context))
        }
    }
}