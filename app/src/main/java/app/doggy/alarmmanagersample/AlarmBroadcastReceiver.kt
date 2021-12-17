package app.doggy.alarmmanagersample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmBroadcastReceiver : BroadcastReceiver() {

    // アラームを検知．
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(MainActivity.ALARM_LOG, "onReceive()")
    }
}