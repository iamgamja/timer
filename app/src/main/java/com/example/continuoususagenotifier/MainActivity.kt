package com.example.continuoususagenotifier

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.continuoususagenotifier.databinding.ActivityMainBinding
import com.example.continuoususagenotifier.service.UsageTimerService
import com.example.continuoususagenotifier.util.PreferenceKeys

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(PreferenceKeys.PREF_NAME, MODE_PRIVATE)

        binding.timeInput.setText(
            prefs.getInt(PreferenceKeys.USAGE_LIMIT_MINUTES, 30).toString()
        )

        binding.startButton.setOnClickListener {
            val minutes = binding.timeInput.text.toString().toIntOrNull() ?: 30
            prefs.edit { putInt(PreferenceKeys.USAGE_LIMIT_MINUTES, minutes) }
            startForegroundService(Intent(this, UsageTimerService::class.java))
        }
    }
}