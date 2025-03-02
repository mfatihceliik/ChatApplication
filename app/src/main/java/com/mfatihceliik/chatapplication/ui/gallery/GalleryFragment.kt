package com.mfatihceliik.chatapplication.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mfatihceliik.chatapplication.SharedViewModel
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery
import com.mfatihceliik.chatapplication.databinding.FragmentGalleryBinding
import com.mfatihceliik.chatapplication.util.GridSpacingItemDecoration
import com.mfatihceliik.chatapplication.util.collectLifecycleStateStarted
import com.mfatihceliik.chatapplication.util.onClickListeners.GallerySetOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment: Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val galleryAdapter = GalleryAdapter()

    companion object {
        const val TAG = "GalleryFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        launchGallery()
        setAdapter()
        setRecyclerview()
        galleryFragmentToFilePickerFragment()
        onClicks()
    }

    private fun setRecyclerview() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
            addItemDecoration(GridSpacingItemDecoration(4, 10, true))
            adapter = galleryAdapter
            //setHasFixedSize(true)
            //setItemViewCacheSize(20)
        }
    }

    private fun setAdapter() {
        collectLifecycleStateStarted(viewModel.gallerySharedFlow) { galleries ->
            galleryAdapter.setGalleryList(galleries)
            binding.gallerySize.text = "(${galleries.size})"
        }
    }

    private fun onClicks() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun launchGallery() {
        viewModel.launchGallery()
    }

    private fun galleryFragmentToFilePickerFragment() {
        galleryAdapter.setGalleryOnClickListener(object: GallerySetOnClickListener {
            override fun onClick(gallery: Gallery) {
                val action = GalleryFragmentDirections.actionGalleryFragmentToFilePickerFragment(gallery)
                findNavController().navigate(action)
            }
        })
    }
}