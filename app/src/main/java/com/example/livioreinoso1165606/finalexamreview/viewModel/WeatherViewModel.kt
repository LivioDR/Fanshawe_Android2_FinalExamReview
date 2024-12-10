package com.example.livioreinoso1165606.finalexamreview.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.livioreinoso1165606.finalexamreview.model.WeatherEntity
import com.example.livioreinoso1165606.finalexamreview.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository):ViewModel() {

    // Using LiveData and caching what weatherData returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    var weatherData: LiveData<List<WeatherEntity>> = repository.weatherData

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(weather: WeatherEntity) = viewModelScope.launch {
        repository.insert(weather)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun fetchData(location:String) = viewModelScope.launch {
        repository.fetchWeatherData(location)
    }
}

class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}