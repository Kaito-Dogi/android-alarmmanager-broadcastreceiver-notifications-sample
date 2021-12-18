package app.doggy.alarmmanagersample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import app.doggy.alarmmanagersample.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var alarmManager: AlarmManager

    private val requestCode: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.setButton.setOnClickListener {
            // Calendarクラスのインスタンスを取得．
            val calendar = Calendar.getInstance()
            // 現在の時間をミリ秒で取得．
            calendar.timeInMillis = System.currentTimeMillis()
            // 5秒後に設定．
            calendar.add(Calendar.SECOND, 5)
            // calendar.set(Calendar.HOGE, amount)で時間，分などを指定できる．

            // アラームがトリガーされたときに開始するペンディングインテント．
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                Intent(this, AlarmBroadcastReceiver::class.java).putExtra(
                    REQUEST_CODE,
                    requestCode
                ),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // AlarmManagerをインスタンス化する．
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            /** アラームをセットする．
             *
             * アラームの頻度は最小限に維持する．
             * アラームのトリガー時間を必要以上に正確に設定しない．
             * 正確なトリガー時刻に基づく反復アラームはうまく調整できないため，可能であれば時刻に基づいてアラームをトリガーすることは避ける．
             *
             * 1回だけ（反復なし）のアラーム：アラームタイプ，トリガー時刻，ペンディングインテントを渡す．
             * set()：推奨．APIレベル19以降では低精度．
             * setExact()：setよりも正確．
             *
             * 繰り返しアラーム：アラームタイプ，トリガー時刻，アラームの間隔，ペンディングインテントを渡す．
             * setInexactRepeating()：推奨．Androidによって複数の不正確な反復アラームが同期され，それらが同時にトリガーされることにより，電池の消耗を抑えることができる．
             * setRepeating()：厳密な時間要件がある場合に使用する．
             *
             * アラームタイプ
             * ELAPSED_REALTIME：デバイスが起動してから指定された時間が経過した後にペンディングインテントを開始するが，デバイスのスリープは解除しない．経過時間にはデバイスがスリープしていた時間が含まれる．
             * ELAPSED_REALTIME_WAKEUP：デバイスが起動してから指定された時間が経過した後にデバイスのスリープを解除してペンディングインテントを開始する．
             * RTC：指定された時刻にペンディングインテントを開始するが，デバイスのスリープは解除しない．
             * RTC_WAKEUP：指定された時刻にデバイスのスリープを解除してペンディングインテントを開始する．
             */
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Log.d(ALARM_LOG, "alarmManager.set()")

            Toast.makeText(this, "5秒後にアラームをセット", Toast.LENGTH_SHORT).show()
        }

        binding.cancelButton.setOnClickListener {
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                Intent(this, AlarmBroadcastReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE
            )

            // アラームをキャンセルする．
            alarmManager.cancel(pendingIntent)
            Log.d(ALARM_LOG, "alarmManager.cancel()")

            Toast.makeText(this, "アラームをキャンセル", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE = "REQUEST_CODE"
        const val ALARM_LOG = "ALARM_LOG"
    }
}