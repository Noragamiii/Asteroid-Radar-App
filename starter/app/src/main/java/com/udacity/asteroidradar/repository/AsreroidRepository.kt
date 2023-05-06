package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Database
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.AsteroidApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsreroidRepository(private val database: AsteroidDatabase) {

    /**
     * A list of asteroid
     */
    val allAsteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAsteroids()) {
                it.asDomainModel()
            }

    /**
     * Start date
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    /**
     * End date
     */

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().minusDays(7)

    /**
     * Search start date
     */
    @RequiresApi(Build.VERSION_CODES.O)
    val dayAsteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAsteroidsDay(startDate.format(DateTimeFormatter.ISO_DATE))) {
                it.asDomainModel()
            }

    @RequiresApi(Build.VERSION_CODES.O)
    val weekAsteroids: LiveData<List<Asteroid>> =
            Transformations.map(
                    database.asteroidDao.getAsteroidsDate(
                            startDate.format(DateTimeFormatter.ISO_DATE),
                            endDate.format(DateTimeFormatter.ISO_DATE)
                    )) {
                it.asDomainModel()
            }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidApiService.AsteroidApi.retrofitService.getAsteroids(API_KEY)
            val result = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDao.insertAll(*result.asDatabaseModel())
        }
    }
}