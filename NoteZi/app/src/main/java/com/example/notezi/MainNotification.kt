package com.example.notezi

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

class MainNotification : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val title: String
        val subtitle: String

        val calendar = Calendar.getInstance()

        when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in arrayOf(6, 9, 12, 15) -> {
                title = "Check your Schedule | NoteZi"
                subtitle = "Hey, Schedule Check. Please Check if you have any Upcoming Schedule"
            }
            in arrayOf(18, 21) -> {
                title = "Check your Tasks | NoteZi"
                subtitle = "Hey, Tasks Check. Please Check if you have any Pending Activities"
            }
            else -> return
        }

        createNotification(context, title, subtitle)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotification(context: Context, title: String, subtitle: String) {
        val channelId = "notezi_channel"
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) // Vibrate for 1 second

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "NoteZi Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
