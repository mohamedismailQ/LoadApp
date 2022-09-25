package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.udacity.ui.DetailActivity

// variables we need

private val NOTIFICATION_ID = 0
private val channelId = "1"
private var file = "FileName"
private var downloadstatus = "Status"
private var btnTitle = "Click For Details"
private var notificationName = "downloadedFiles"
// here i give importance for the notification
private var importanceLvl = NotificationManager.IMPORTANCE_DEFAULT
// here i initialize notification channel for versions
@RequiresApi(Build.VERSION_CODES.O)
// and pass to it channelId,notificationName,importanceLvl as a params
private val channel = NotificationChannel(channelId, notificationName, importanceLvl)


fun NotificationManager.sendNotification(
    messageBody: String,
    status: String,
    applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(file, messageBody)
    contentIntent.putExtra(downloadstatus, status)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
// here is the builder for the notification
    val builder = NotificationCompat.Builder(applicationContext, channelId)
// here we pass the importance info for builder to make a notification body
        .setSmallIcon(R.drawable.cloud)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    // i added button by call builder.addAction
    builder.addAction(R.drawable.cloud, btnTitle, contentPendingIntent)

    notify(NOTIFICATION_ID, builder.build())
}
// here i create the channel for notification
fun createNotificationChannel(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
