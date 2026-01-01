package com.example.continuoususagenotifier.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.example.continuoususagenotifier.notification.NotificationHelper
import com.example.continuoususagenotifier.receiver.ScreenStateReceiver
import com.example.continuoususagenotifier.util.PreferenceKeys
import kotlinx.coroutines.*

class UsageTimerService : Service() {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var elapsedMs = 0L
    private var screenOn = true

    override fun onCreate() {
        super.onCreate()

        val notifier = NotificationHelper(this)
        startForeground(NotificationHelper.FOREGROUND_ID, notifier.createForegroundNotification())

        val receiver = ScreenStateReceiver(
            onScreenOn = { screenOn = true },
            onScreenOff = {
                screenOn = false
                elapsedMs = 0L
            }
        )

        registerReceiver(receiver, IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        })

        scope.launch {
            val prefs = getSharedPreferences(PreferenceKeys.PREF_NAME, MODE_PRIVATE)
            while (isActive) {
                delay(1000)
                if (!screenOn) continue

                elapsedMs += 1000
                val limitMs = prefs.getInt(
                    PreferenceKeys.USAGE_LIMIT_MINUTES, 30
                ) * 60 * 1000L

                if (elapsedMs >= limitMs) {
                    notifier.showUsageAlert()
                    elapsedMs = 0L
                }
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}