package com.example.livioreinoso1165606.finalexamreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherEntity(
    @PrimaryKey val uid:String,
    @ColumnInfo(name = "City") val city: String,
    @ColumnInfo(name = "Temp C") val temp_c: Double,
    @ColumnInfo(name = "Feels Like") val feels_like:Double,
    @ColumnInfo(name = "Condition") val condition:String,
    @ColumnInfo(name = "Time") val localtime:String)