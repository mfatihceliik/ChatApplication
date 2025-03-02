package com.mfatihceliik.chatapplication.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mfatihceliik.chatapplication.R
import com.mfatihceliik.chatapplication.data.entity.remote.user.LoginRequest
import com.mfatihceliik.chatapplication.databinding.LoginFragmentBinding
import com.mfatihceliik.chatapplication.util.Resource
import com.mfatihceliik.chatapplication.util.collectLatestLifecycleFlow
import com.mfatihceliik.chatapplication.util.gone
import com.mfatihceliik.chatapplication.util.hide
import com.mfatihceliik.chatapplication.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
    }

    private fun initialize() {
        onClicks()
        textChangeListeners()
        checkEmailCorrectFormatImage()
    }

    private fun textChangeListeners() {
        binding.emailEditText.addTextChangedListener { editable ->
            emailEditTextChange(editable = editable)
        }
    }

    private fun onClicks() {
        binding.loginButton.setOnClickListener { view ->
            loginOperation(view)
        }

        binding.passwordEyeImageButton.setOnClickListener {
            changePasswordEyeImage()
        }
    }

    private fun emailEditTextChange(editable: Editable?) {
        editable?.let {
            viewModel.changeEmailTextState(it.toString())
        }
    }

    private fun checkEmailCorrectFormatImage() {
        collectLatestLifecycleFlow(viewModel.isValidEmailAddressStateFlow) {
            if(it) {
                changeEmailImageButtonColor(true)
            }else {
                changeEmailImageButtonColor(false)
            }
        }
    }

    private fun changePasswordEyeImage() {
        if(viewModel.passwordEyeShowStateFlow.value) {
            viewModel.changePasswordEyeState(false)
            binding.passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            changePasswordEyeColorFilter(true)
        }else {
            viewModel.changePasswordEyeState(true)
            binding.passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            changePasswordEyeColorFilter(false)
        }
    }

    private fun loginOperation(view: View) {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        if(email != "" && password != ""){

            collectLatestLifecycleFlow(viewModel.isValidEmailAddressStateFlow) {
                if(it) {
                    login(email = email, password = password, view = view)
                }else {
                    val message = "Email format is not true."
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Close"){}.show()
                }
            }
        }else {
            val message = "Email or password are not empty."
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Close"){}.show()
        }
    }

    private fun login(email: String, password: String, view: View) {
        viewModel.login(loginRequest = LoginRequest(email = email, password = password)).observe(viewLifecycleOwner) {
            when(it.status){
                Resource.Status.LOADING -> binding.progressBar.show()
                Resource.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    val message = it.message ?: it.data?.message ?: "Successfully logged in."
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Close"){}.show()
                    it.data?.let { data ->
                        viewModel.saveUser(data.user)
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.gone()
                    val message = it.message ?: it.data?.message ?: "an unexpected error occurred."
                    Log.v(TAG, message)
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Close"){}.show()
                }
            }
        }
    }

    private fun changePasswordEyeColorFilter(isTrueFormat: Boolean) {
        if(isTrueFormat) binding.passwordEyeImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.warning))
        else binding.passwordEyeImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.success))
    }

    private fun changeEmailImageButtonColor(isTrueFormat: Boolean) {
        if(isTrueFormat) binding.emailCorrectFormatImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.success))
        else binding.emailCorrectFormatImageButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.warning))
    }
}