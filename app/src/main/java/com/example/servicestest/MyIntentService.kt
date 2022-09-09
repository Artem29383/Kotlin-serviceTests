package com.example.servicestest

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyIntentService : IntentService(NAME) {
    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("Timer $i")
        }
    }

    override fun onCreate() {
        super.onCreate()
        log("OnCreate")
        setIntentRedelivery(true) //эквивалентно START_REDELIVER_INTENT в обычном сервисе
        createNotifyChannel()
        startForeground(NOTIFY_ID, createNotify())
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "$NAME: $message")
    }

    private fun createNotifyChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotify() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Notify foreground")
        .setContentText("This is message for $NAME service")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_foreground"
        private const val NOTIFY_ID = 777
        private const val NAME = "MyIntentService"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}