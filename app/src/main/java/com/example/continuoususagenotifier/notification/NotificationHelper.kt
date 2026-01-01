package com.example.continuoususagenotifier.notification

import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.continuoususagenotifier.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "usage_channel"
        const val FOREGROUND_ID = 1
        const val ALERT_ID = 2
    }

    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "연속 사용 알림",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }

    fun createForegroundNotification(): Notification =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("사용 시간 감지 중")
            .setContentText("연속 사용 시간을 측정하고 있습니다.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

    fun showUsageAlert() {
        manager.notify(
            ALERT_ID,
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("사용 시간 경고")
                .setContentText("설정한 연속 사용 시간이 지났습니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
        )
    }
}