package com.example.livioreinoso1165606.finalexamreview.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.livioreinoso1165606.finalexamreview.R
import com.example.livioreinoso1165606.finalexamreview.database.WeatherRoomDatabase
import com.example.livioreinoso1165606.finalexamreview.databinding.ActivityWeatherBinding
import com.example.livioreinoso1165606.finalexamreview.repository.WeatherRepository
import com.example.livioreinoso1165606.finalexamreview.viewModel.WeatherViewModel
import com.example.livioreinoso1165606.finalexamreview.viewModel.WeatherViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityWeatherBinding

    private val database by lazy {
        WeatherRoomDatabase.getDatabase(this)
    }

    private val repository by lazy {
        WeatherRepository(database.weatherDao())
    }

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(repository)
    }

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

        val weatherData = weatherViewModel.weatherData.observe(this) { data ->
            // guarding if the weatherData list is empty
            if (data.size == 0){
                lifecycleScope.launch {
                    WeatherRoomDatabase.populateDatabase(database.weatherDao())
                }
                return@observe
            }

            // TODO: change this from single display to recyclerView
            val lastData = data.first()

            binding.resultText.text = """
                Welcome ${auth.currentUser?.uid}!
            
            Current data for ${lastData.city}
            at ${lastData.localtime}
            
            Temperature: ${lastData.temp_c}°C
            Feels like: ${lastData.feels_like}°C
            
            Current condition is ${lastData.condition}
            
            """.trimIndent()
        }

        binding.searchButton.setOnClickListener{
            val city = binding.weatherSearch.text.toString()
            lifecycleScope.launch {
                weatherViewModel.fetchData(city)
            }
        }
    }
}