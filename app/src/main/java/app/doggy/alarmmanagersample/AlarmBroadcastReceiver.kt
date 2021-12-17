package app.doggy.alarmmanagersample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmBroadcastReceiver : BroadcastReceiver() {

    // アラームを検知．
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "onReceive()", Toast.LENGTH_SHORT).show()
    }
}