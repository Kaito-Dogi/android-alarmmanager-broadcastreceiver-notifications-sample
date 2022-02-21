package app.doggy.alarmmanagersample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmBroadcastReceiver : BroadcastReceiver() {

    // アラームを検知。
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(MainActivity.ALARM_LOG, "onReceive()")

        val requestCode = intent?.getIntExtra(MainActivity.REQUEST_CODE_KEY, 0) as Int

        // 通知のタップアクション（ユーザーが通知をタップしたときにMainActivityを開く）。
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        // 通知のコンテンツの設定。
        // チャンネルIDの指定が必要。
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            // 小さなアイコン。
            .setSmallIcon(R.drawable.ic_alarm)
            // タイトル。
            .setContentTitle(context.getString(R.string.notification_title))
            // 本文テキスト。
            .setContentText(context.getString(R.string.notification_text))
            // 通知の優先度（Android 7.1以下）。
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // 通知のタップアクションを設定する。
            .setContentIntent(pendingIntent)
            // ユーザーが通知をタップすると、配信された通知が自動的に消去される。
            .setAutoCancel(true)

        // チャンネルを作成して重要度を設定する（Android 8.0以上）。
        // 通知をチャンネルに分類することで、チャンネルごとに通知の設定を変えられる。
        // SDK_INTバージョンの条件によってブロックする。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.channel_name),
                // 通知の優先度（Android 8.0以上）。
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.channel_description)
                // バイブレーションの設定。
                enableVibration(true)
                // アプリのアイコンにバッジを付けるかどうかの設定。
                setShowBadge(true)
                // LEDの設定。
                enableLights(true)
                // ロック画面での表示形式。
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }

            // チャンネルをシステムに登録する。
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // 通知を表示する。
        with(NotificationManagerCompat.from(context)) {
            // notificationId：一意の整数。
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    companion object {
        private const val CHANNEL_ID = "DEFAULT"
        private const val NOTIFICATION_ID = 9999
    }
}