package com.example.foreverfreedictionary.util

import android.R
import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.foreverfreedictionary.ui.broadcastReceiver.OnActionReminderNotificationBroadCastReceiver


class NotificationUtil(context: Context) : ContextWrapper(context) {
    companion object{
        // This is the Notification Channel ID. More about this in the next section
        const val NOTIFICATION_CHANNEL_ID = "channel_reminder_id"
        //User visible Channel Name
        const val CHANNEL_NAME = "Notification Reminder"

        const val REMINDER_NOTIFICATION_ID = 101

        const val ARG_REMINDERS_QUERY_LIST = "ARG_REMINDERS_QUERY_LIST"
        const val ACTION_REMINDER_SET_REMINDED = "ACTION_REMINDER_SET_REMINDED"
        const val ACTION_REMINDER_SET_REMINDED_AND_OPEN_REMINDER_SCREEN = "ACTION_REMINDER_SET_REMINDED_AND_OPEN_REMINDER_SCREEN"

        const val ON_TAP_REMINDER_NOTIFICATION_PENDING_INTENT_REQUEST_CODE = 1001
        const val ON_DELETE_REMINDER_NOTIFICATION_PENDING_INTENT_REQUEST_CODE = 1002
    }

    val mManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    init {
        createChannels()
    }

    private fun createChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true)
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true)
            //Sets the color of Notification Light
            notificationChannel.lightColor = Color.GREEN
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.vibrationPattern = longArrayOf(500, 500, 500, 500, 500)
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            mManager.createNotificationChannel(notificationChannel)
        }

    }

    fun createReminderNotification(
        title: String,
        content: String,
        listQuery: MutableList<String>
    ): NotificationCompat.Builder {
        //Notification Channel ID passed as a parameter here will be ignored for all the Android versions below 8.0
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_lock_idle_alarm)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        builder.setVibrate(longArrayOf(500, 500, 500, 500))
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//Lock Screen Visibility
        builder.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_lock_idle_alarm))

        //This intent will be fired when the notification is tapped
        val intent = Intent(this, OnActionReminderNotificationBroadCastReceiver::class.java)
        intent.action = ACTION_REMINDER_SET_REMINDED_AND_OPEN_REMINDER_SCREEN
        intent.putStringArrayListExtra(ARG_REMINDERS_QUERY_LIST, ArrayList(listQuery))
        val pendingIntent = PendingIntent.getBroadcast(this, ON_TAP_REMINDER_NOTIFICATION_PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //Following will set the tap action
        builder.setContentIntent(pendingIntent)

        //Action Buttons for your notification
//        val buttonIntent = Intent(this, MainActivity::class.java)
//        val buttonPendingIntent = PendingIntent.getBroadcast(this, 1002, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        builder.addAction( R.drawable.ic_lock_idle_alarm, resources.getString(R.string.ok), buttonPendingIntent)

//        builder.setStyle(NotificationCompat.BigTextStyle()
//                .bigText(content))

//        builder.setStyle(NotificationCompat.BigPictureStyle()
//            .bigPicture(BitmapFactory.decodeResource(application.resources, R.drawable.icon)))

//        builder.setStyle(NotificationCompat.InboxStyle()
//            .addLine("This is the first line")
//            .addLine("This is the second line")
//            .addLine("This is the third line"))

        val intentDelete = Intent(this, OnActionReminderNotificationBroadCastReceiver::class.java)
        intentDelete.putExtra("resultCode", Activity.RESULT_OK)
        intentDelete.action = ACTION_REMINDER_SET_REMINDED
        intentDelete.putStringArrayListExtra(ARG_REMINDERS_QUERY_LIST, ArrayList(listQuery))
        val pendingIntentDelete = PendingIntent.getBroadcast(this.applicationContext, ON_DELETE_REMINDER_NOTIFICATION_PENDING_INTENT_REQUEST_CODE, intentDelete, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setDeleteIntent(pendingIntentDelete)
        builder.setAutoCancel(true)
        return builder
    }
}