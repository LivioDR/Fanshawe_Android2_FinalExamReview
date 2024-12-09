package com.example.livioreinoso1165606.finalexamreview.network

import com.example.livioreinoso1165606.finalexamreview.model.WeatherResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    // definition of the function getWeather as a GET request
    @GET("v1/current.json?key=be3b8fcd0d464568880230428240912&aqi=no")
    // declaring this as a suspend function to run in a coroutine
    suspend fun getWeather(
        // all Query parameters are passed as arguments to the function
        // if we needed to pass path variables we can use @Path instead
        @Query("q") location:String,
        // which will then return a RemoteResult instance as defined in the model folder
    ): WeatherResult
}

// this object will be called from the main activity to create an instance
// of the RetrofitService declared as an interface above
object RetrofitServiceFactory {
    fun makeRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            // we pass the base URL to the Retrofit builder
            .baseUrl("https://api.weatherapi.com/")
            // a converter factory to convert between JSON and objects
            .addConverterFactory(GsonConverterFactory.create())
            // and then we call the build method to create the instance of this class
            .build().create(RetrofitService::class.java)
    }
}