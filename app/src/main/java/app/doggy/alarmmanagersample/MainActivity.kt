package app.doggy.alarmmanagersample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.doggy.alarmmanagersample.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.setButton.setOnClickListener {
            // 通知を送る時刻を，ボタンを押した10秒後に設定．
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.SECOND, 10)

            //
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Intent(this, AlarmBroadcastReceiver::class.java),
                0
            )

            // アラームをセットする．
            val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
}