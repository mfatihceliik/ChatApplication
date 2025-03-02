package com.mfatihceliik.chatapplication.ui.file

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mfatihceliik.chatapplication.R
import com.mfatihceliik.chatapplication.SharedViewModel
import com.mfatihceliik.chatapplication.data.entity.enums.GalleryMediaType
import com.mfatihceliik.chatapplication.data.entity.local.gallery.Gallery
import com.mfatihceliik.chatapplication.databinding.FilePickerFragmentBinding
import com.mfatihceliik.chatapplication.util.collectLifecycleStateStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilePickerFragment: Fragment() {

    private lateinit var binding: FilePickerFragmentBinding
    private val viewModel: FilePickerViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: FilePickerFragmentArgs by navArgs()


    private lateinit var mediaController: MediaController

    companion object {
        private const val TAG = "FilePickerFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilePickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        setMediaController()
        setMediaWithUri(args.gallery)
        onClicks()
        observeConversationId()
    }

    private fun setMediaController() {
        mediaController = MediaController(this.context)
    }

    private fun onClicks() {
        binding.backImageButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.closeImageButton.setOnClickListener {
            //findNavController().navigate(R.id.action_filePickerFragment_to_chatFragment)
            findNavController().popBackStack(R.id.chatFragment, false)
        }
        binding.sendButton.setOnClickListener {
            sendMedia(args.gallery)
            findNavController().popBackStack(R.id.chatFragment, false)
        }

    }

    private fun sendMedia(gallery: Gallery) {
        when(gallery.type) {
            GalleryMediaType.IMAGE -> sendImageMessage(uri = gallery.uri)
            GalleryMediaType.VIDEO -> sendVideoMessage(uri = gallery.uri)
        }
    }

    private fun sendVideoMessage(uri: Uri) {

    }

    private fun sendImageMessage(uri: Uri) {
        val text = binding.messageEditText.text?.toString()
        val createImageMessage = viewModel.createImageMessage(text = text, uri = uri)
        sharedViewModel.addMessageToRecyclerView(message = createImageMessage)
        //sharedViewModel.saveMessageToLocalDb(message = imageMessage)
        sharedViewModel.sendImageMessage(message = createImageMessage)
    }

    private fun setMediaWithUri(gallery: Gallery) {
        when(gallery.type) {
            GalleryMediaType.IMAGE -> image(uri = gallery.uri)
            GalleryMediaType.VIDEO -> video(uri = gallery.uri)
        }
    }

    private fun image(uri: Uri) {
        binding.videoView.visibility = View.INVISIBLE
        binding.photoImageView.visibility = View.VISIBLE
        Glide.with(requireContext()).load(uri).into(binding.photoImageView)
    }

    private fun video(uri: Uri) {
        binding.photoImageView.visibility = View.INVISIBLE
        binding.videoView.visibility = View.VISIBLE
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(uri)
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        binding.videoView.start()
    }

    private fun observeConversationId() {
        sharedViewModel.conversationId.observe(viewLifecycleOwner) { conversationId ->
            viewModel.setConversationId(conversationId = conversationId)
        }
    }
}