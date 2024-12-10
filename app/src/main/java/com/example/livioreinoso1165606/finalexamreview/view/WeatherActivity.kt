package com.example.livioreinoso1165606.finalexamreview.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livioreinoso1165606.finalexamreview.R
import com.example.livioreinoso1165606.finalexamreview.database.WeatherRoomDatabase
import com.example.livioreinoso1165606.finalexamreview.databinding.ActivityWeatherBinding
import com.example.livioreinoso1165606.finalexamreview.model.WeatherEntity
import com.example.livioreinoso1165606.finalexamreview.recyclerView.WeatherListAdapter
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

    private val weatherList = mutableListOf<WeatherEntity>()
    private lateinit var recyclerView: RecyclerView

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

        // getting the auth info to display the user ID
        auth = Firebase.auth

        binding.resultText.text = """
            Welcome ${auth.currentUser?.uid}!
            Type a city name and press the 'Search' button to start
        """.trimIndent()

        // adding the logout function to the logout button
        binding.logout.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }

        // setting the recycler view
        recyclerView = findViewById(R.id.recyclerView)
        // and the adapter
        val adapter = WeatherListAdapter(weatherList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // then observing the data for changes
        val weatherData = weatherViewModel.weatherData.observe(this) { data ->
            // guarding if the weatherData list is empty
            if (data.size == 0){
                lifecycleScope.launch {
                    WeatherRoomDatabase.populateDatabase(database.weatherDao())
                }
                return@observe
            }

            // removing the instructions from the text view
            binding.resultText.text = "Welcome ${auth.currentUser?.uid}"

            // and updating the weather list in memory to then notify the recycler view
            data.let {
                weatherList.clear()
                weatherList.addAll(it)
                recyclerView?.adapter?.notifyDataSetChanged()
            }


        }

        binding.searchButton.setOnClickListener{
            val city = binding.weatherSearch.text.toString()
            lifecycleScope.launch {
                weatherViewModel.fetchData(city)
            }
        }
    }
}