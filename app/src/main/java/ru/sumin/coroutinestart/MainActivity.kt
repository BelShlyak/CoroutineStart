package ru.sumin.coroutinestart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sumin.coroutinestart.databinding.ActivityMainBinding
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            binding.progress.isVisible = true
            binding.buttonLoad.isEnabled = false
            lifecycleScope.launch {
                try {
                    val cityDeferred = async {
                        loadCity()
                    }
                    val tempDeferred = async {
                        loadTemperature()
                    }
                    val city = cityDeferred.await()
                    val temperature = tempDeferred.await()
                    binding.tvLocation.text = city
                    binding.tvTemperature.text = temperature.toString()
                    showToastCityAndTemperature(city, temperature)
                } finally {
                    binding.progress.isVisible = false
                    binding.buttonLoad.isEnabled = true

                }
            }
        }
    }

    private fun showToastCityAndTemperature(city: String, temperature: Int) {
        Toast.makeText(
            this,
            ("City: $city, Temperature: $temperature"),
            Toast.LENGTH_SHORT
        ).show()
    }
}

private suspend fun loadCity(): String {
    delay(5000.milliseconds)
    return "Moscow"
}

private suspend fun loadTemperature(): Int {
    delay(5000.milliseconds)
    return 17
}
