package app.doggy.alarmmanagersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.doggy.alarmmanagersample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
    }
}