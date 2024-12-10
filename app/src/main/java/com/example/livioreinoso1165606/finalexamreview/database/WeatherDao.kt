package com.example.livioreinoso1165606.finalexamreview.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.livioreinoso1165606.finalexamreview.model.WeatherEntity

@Dao
interface WeatherDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM weather_data ORDER BY uid ASC")
    fun getAllWeatherSearches(): LiveData<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: WeatherEntity)

    @Query("DELETE FROM weather_data")
    suspend fun deleteAll()
}