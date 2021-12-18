package app.doggy.alarmmanagersample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class AlarmBroadcastReceiver : BroadcastReceiver() {

    // アラームを検知．
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(MainActivity.ALARM_LOG, "onReceive()")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val requestCode = intent?.getIntExtra(MainActivity.REQUEST_CODE, 0) as Int

            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(R.string.notification_id, builder.build())
        }
    }

    companion object {
        private const val CHANNEL_ID = "DEFAULT"
    }
}