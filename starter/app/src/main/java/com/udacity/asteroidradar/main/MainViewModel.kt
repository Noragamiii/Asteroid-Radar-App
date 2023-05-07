package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.FilterAsteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApiService
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsreroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * create database and get database
     */
    private val database = getDatabase(application)
    private val asteroidRepository = AsreroidRepository(database)

    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            //refreshPictureOfDay()
        }
    }

    /**
     * Information picture
     */
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    /**
     * Information asteroid
     */
    private val _detailAsteroid = MutableLiveData<Asteroid>()
    val detailAsteroid: LiveData<Asteroid>
        get() = _detailAsteroid

    private var _filterAsteroid = MutableLiveData(FilterAsteroid.ALL)

    /**
     * Get list asteroid filter
     */
    @RequiresApi(Build.VERSION_CODES.O)
    val asteroidListFilter = Transformations.switchMap(_filterAsteroid) {
        when (it!!) {
            FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids
            FilterAsteroid.TODAY -> asteroidRepository.dayAsteroids
            else -> asteroidRepository.allAsteroids
        }
    }

    /**
     * Navigate to detail
     */
    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid>()
    val navigateToDetailAsteroid: LiveData<Asteroid>
        get() = _navigateToDetailAsteroid
    private suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            _pictureOfDay.postValue(AsteroidApiService.AsteroidApi.retrofitService.getPictureOfTheDay(API_KEY))
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToDetailAsteroid.value = null
    }

    fun onChangeFilter(filter: FilterAsteroid) {
        _filterAsteroid.postValue(filter)
    }
}