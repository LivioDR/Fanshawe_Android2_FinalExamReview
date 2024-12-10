package com.example.livioreinoso1165606.finalexamreview.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.livioreinoso1165606.finalexamreview.database.WeatherDao
import com.example.livioreinoso1165606.finalexamreview.model.WeatherEntity
import com.example.livioreinoso1165606.finalexamreview.model.WeatherResult
import com.example.livioreinoso1165606.finalexamreview.network.RetrofitServiceFactory
import java.util.UUID

class WeatherRepository(private val weatherDao: WeatherDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    var weatherData: LiveData<List<WeatherEntity>> = weatherDao.getAllWeatherSearches()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(weather: WeatherEntity) {
        weatherDao.insert(weather)
    }

    suspend fun deleteAll() {
        weatherDao.deleteAll()
    }

    // Network handling
    val service = RetrofitServiceFactory.makeRetrofitService()

    suspend fun fetchWeatherData(location:String){
        val currentWeather:WeatherResult = service.getWeather(location)

        val weatherEntry = WeatherEntity(
            UUID.randomUUID().toString(),
            "${currentWeather.location.name.toString()}, ${currentWeather.location.region}, ${currentWeather.location.country}",
            currentWeather.current.temp_c,
            currentWeather.current.feelslike_c,
            currentWeather.current.condition.text,
            currentWeather.location.localtime)

        insert(weatherEntry)
    }


}