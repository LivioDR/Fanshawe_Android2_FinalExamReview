package com.example.livioreinoso1165606.finalexamreview.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.livioreinoso1165606.finalexamreview.R
import com.example.livioreinoso1165606.finalexamreview.databinding.ActivityWeatherBinding
import com.example.livioreinoso1165606.finalexamreview.viewModel.WeatherViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WeatherActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityWeatherBinding
    private val weatherViewModel:WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.resultText.text = """
            Welcome ${auth.currentUser?.uid}!
            Type a city name and press the 'Search' button to start
        """.trimIndent()

        binding.logout.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }

        val weatherData = weatherViewModel.getWeatherData().observe(this) { data ->
            binding.resultText.text = """
                Welcome ${auth.currentUser?.uid}!
            
            Current data for ${data.location.name}, ${data.location.region}, ${data.location.country}
            at ${data.location.localtime}
            
            Temperature: ${data.current.temp_c}°C
            Feels like: ${data.current.feelslike_c}°C
            
            Current condition is ${data.current.condition.text}
            
            """.trimIndent()
        }

        binding.searchButton.setOnClickListener{
            val city = binding.weatherSearch.text.toString()
            weatherViewModel.fetchWeatherData(city)
        }
    }
}