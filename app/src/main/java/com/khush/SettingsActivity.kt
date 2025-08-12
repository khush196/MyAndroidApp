// SettingsActivity.kt
package com.khush.devicemapper

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeSwitch: SwitchCompat
    private lateinit var intervalSeekBar: SeekBar
    private lateinit var intervalLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        themeSwitch = findViewById(R.id.switch_theme)
        intervalSeekBar = findViewById(R.id.seekbar_interval)
        intervalLabel = findViewById(R.id.interval_value)

        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        themeSwitch.isChecked = isDarkMode
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        themeSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val currentInterval = sharedPreferences.getInt("scan_interval", 5)
        intervalSeekBar.progress = currentInterval
        intervalLabel.text = "$currentInterval sec"

        intervalSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                intervalLabel.text = "$progress sec"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sharedPreferences.edit().putInt("scan_interval", intervalSeekBar.progress).apply()
            }
        })
    }
}
