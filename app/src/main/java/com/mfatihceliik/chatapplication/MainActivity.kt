package com.mfatihceliik.chatapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fmssbilisimtech.notification.NotificationChannelConfig
import com.fmssbilisimtech.notification.NotificationIntegration
import com.fmssbilisimtech.notification.NotificationListener
import com.google.android.material.snackbar.Snackbar
import com.mfatihceliik.chatapplication.databinding.ActivityMainBinding
import com.mfatihceliik.chatapplication.util.collectLifecycleStateStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NotificationListener.FirebaseTokenListener {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var notificationIntegration: NotificationIntegration


    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        //connectionStatus(view = view)
        //setupNotificationIntegration()
    }

    private fun setupNotificationIntegration() {
        val channels = listOf(
            NotificationChannelConfig("profile", "profile_channel_id", "Profile"),
            NotificationChannelConfig("payment", "payment_channel_id", "Payment QR")
        )

        notificationIntegration = NotificationIntegration(context = this, channels = channels, notificationIcon = R.drawable.ic_launcher_background)

        NotificationListener.addListener(this)

        if (Build.VERSION.SDK_INT >= TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationIntegration.checkAndCreateNotificationChannels(this)
            } else {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
            }
        } else {
            notificationIntegration.checkAndCreateNotificationChannels(this)
            }
        }

    private fun connectionStatus(view: View) {
        collectLifecycleStateStarted(viewModel.connectionStatus) { connectionStatus ->
            if (connectionStatus) {
                Snackbar.make(view, "Connected !", Snackbar.LENGTH_LONG).show()
            }else {
                viewModel.reConnect()
                Snackbar.make(view, "Reconnecting", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.v(TAG, token)
    }
}