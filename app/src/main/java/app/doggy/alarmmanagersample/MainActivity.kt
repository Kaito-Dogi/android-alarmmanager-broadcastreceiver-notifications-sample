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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.setButton.setOnClickListener {
            // Calendarクラスのインスタンスを取得。
            val calendar = Calendar.getInstance()
            // 現在の時間をミリ秒で取得。
            calendar.timeInMillis = System.currentTimeMillis()
            // 5秒後に設定。
            calendar.add(Calendar.SECOND, 5)

            /**
             * calendar.set(Calendar.CONSTANT, amount)で日付と時刻を指定できる。
             *
             * 例：2021年12月19日午後3時20分 日本標準時
             * calendar.set(Calendar.YEAR, 2021)
             * calendar.set(Calendar.MONTH, 11)
             * calendar.set(Calendar.DATE, 19)
             * calendar.set(Calendar.AM_PM,Calendar.PM)
             * calendar.set(Calendar.HOUR, 3)
             * calendar.set(Calendar.MINUTE, 20)
             * calendar.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
             */

            // アラームがトリガーされたときに開始するペンディングインテント。
            val pendingIntent = PendingIntent.getBroadcast(
                this@MainActivity,
                // requestCodeの値で、ペンディングインテントを識別する。
                REQUEST_CODE,
                // 明示的なブロードキャスト。
                Intent(this, AlarmBroadcastReceiver::class.java).putExtra(
                    REQUEST_CODE_KEY,
                    REQUEST_CODE
                ),
                PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
            )

            // AlarmManagerをインスタンス化する。
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

            /**
             * アラームをセットする。
             *
             * アラームの頻度は最小限に維持する。
             * アラームのトリガー時間を必要以上に正確に設定しない。
             * 正確なトリガー時刻に基づく反復アラームはうまく調整できないため、可能であれば時刻に基づいてアラームをトリガーすることは避ける。
             *
             * 1回だけ（反復なし）のアラーム：アラームタイプ，トリガー時刻，ペンディングインテントを渡す。
             * set()：推奨．APIレベル19以降では低精度。
             * setExact()：setよりも正確。
             *
             * 繰り返しアラーム：アラームタイプ，トリガー時刻，アラームの間隔，ペンディングインテントを渡す。
             * setInexactRepeating()：推奨。Androidによって複数の不正確な反復アラームが同期され、それらが同時にトリガーされることにより、電池の消耗を抑えることができる。
             * setRepeating()：厳密な時間要件がある場合に使用する。
             *
             * アラームタイプ
             * ELAPSED_REALTIME：デバイスが起動してから指定された時間が経過した後にペンディングインテントを開始するが、デバイスのスリープは解除しない。経過時間にはデバイスがスリープしていた時間が含まれる。
             * ELAPSED_REALTIME_WAKEUP：デバイスが起動してから指定された時間が経過した後にデバイスのスリープを解除してペンディングインテントを開始する。
             * RTC：指定された時刻にペンディングインテントを開始するが、デバイスのスリープは解除しない。
             * RTC_WAKEUP：指定された時刻にデバイスのスリープを解除してペンディングインテントを開始する。
             */
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Log.d(ALARM_LOG, "alarmManager.set()")

            Toast.makeText(this, "5秒後にアラームをセット", Toast.LENGTH_SHORT).show()
            /**
             * 他にも以下のメソッドがある。
             *
             * cancel：設定したアラームの取り消し。
             * setTime：ミリ秒での時間設定。
             * setTimeZone：タイムゾーンの設定。
             */
        }

        binding.cancelButton.setOnClickListener {
            val pendingIntent = PendingIntent.getBroadcast(
                this@MainActivity,
                REQUEST_CODE,
                Intent(this, AlarmBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
            )

            // アラームをキャンセルする。
            alarmManager.cancel(pendingIntent)
            Log.d(ALARM_LOG, "alarmManager.cancel()")

            Toast.makeText(this, "アラームをキャンセル", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_KEY = "REQUEST_CODE"
        const val REQUEST_CODE = 1
        const val ALARM_LOG = "ALARM_LOG"
    }
}