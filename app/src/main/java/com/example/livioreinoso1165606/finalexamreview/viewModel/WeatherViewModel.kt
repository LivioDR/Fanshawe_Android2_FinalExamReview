package com.example.livioreinoso1165606.finalexamreview.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livioreinoso1165606.finalexamreview.model.WeatherResult
import com.example.livioreinoso1165606.finalexamreview.network.RetrofitServiceFactory
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {

    var weatherData = MutableLiveData<WeatherResult>()

    fun getWeatherData(): LiveData<WeatherResult> = weatherData

    val service = RetrofitServiceFactory.makeRetrofitService()

    fun fetchWeatherData(location:String){
        viewModelScope.launch {
            weatherData.value = service.getWeather(location)
        }
    }

}