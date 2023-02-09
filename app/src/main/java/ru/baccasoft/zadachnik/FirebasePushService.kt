package ru.baccasoft.zadachnik

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import ru.baccasoft.zadachnik.data.SessionDataStorage
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FirebasePushService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var sessionDataStorage: SessionDataStorage

    override fun onMessageReceived(push: RemoteMessage) {
        Timber.d("--> PUSH ${push.data}")
        throwNotification(push)
    }

    private fun throwNotification(push: RemoteMessage) {
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (push.notification == null) return

        val idNotification = Random.nextInt()
        val builder = NotificationCompat.Builder(applicationContext, getChannelId())
            .setContentTitle(push.notification?.title)
            .setSmallIcon(R.drawable.logo2)
            .setStyle(NotificationCompat.BigTextStyle().bigText(push.notification?.body))
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    idNotification,
                    MainActivity.newPushIntent(applicationContext, push.data),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            builder.setChannelId(getChannelId())
        }
        notificationManager.notify(idNotification, builder.build())

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM Token: $token")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            getChannelId(),
            getChannelName(),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(true)
            lightColor = Color.WHITE
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun getChannelId() = getString(R.string.app_bundle_name)
    private fun getChannelName() = getString(R.string.app_name)







    companion object{
        private const val TAG = "FirebasePushService"
    }
}