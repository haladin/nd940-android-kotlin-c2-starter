package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.getTodayAsLong
import com.udacity.asteroidradar.utils.getWeekAsLong
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    val imageOfTheDay: LiveData<PictureOfDay>
        get() = _imageOfTheDay

    init {
        refreshData()
        showWeekAsteroids()

    }

    fun showAllAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroids()
                .collect { asteroids ->
                    _asteroids.value = asteroids.asDomainModel()
                }
        }
    }

    fun showTodayAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getTodayAsteroids(getTodayAsLong())
                .collect { asteroids ->
                    _asteroids.value = asteroids.asDomainModel()
                }
        }
    }

    fun showWeekAsteroids(){
        viewModelScope.launch {
            database.asteroidDao.getWeekAsteroids(getTodayAsLong(), getWeekAsLong())
                .collect { asteroids ->
                    _asteroids.value = asteroids.asDomainModel()
                }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()

                _imageOfTheDay.value = asteroidRepository.getImageOfTheDay()
            } catch (e: Exception) {
                e.message?.let {
                    Log.e("Retrofit", it)
                }
            }
        }
    }

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()

    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    fun displayDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}