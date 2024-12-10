package com.example.livioreinoso1165606.finalexamreview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.livioreinoso1165606.finalexamreview.model.WeatherEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherRoomDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): WeatherRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                // Creation of WeatherRoomDatabase
                val instance = Room.databaseBuilder(
                    context.applicationContext,  // context
                    WeatherRoomDatabase::class.java, // Database class needed to create
                    "weather_database" // Database name
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()

                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WeatherDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.weatherDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more weather results, just add them.
         */
        suspend fun populateDatabase(weatherDao: WeatherDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            weatherDao.deleteAll()

            val weather = WeatherEntity(
                UUID.randomUUID().toString(),
                "London, Ontario, Canada",
                3.2,
                1.7,
                "Fog",
                "2024-12-10 14:34")
            weatherDao.insert(weather)

        }
    }
}