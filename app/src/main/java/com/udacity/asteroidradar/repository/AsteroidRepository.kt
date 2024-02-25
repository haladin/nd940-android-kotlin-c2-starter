package com.udacity.asteroidradar.repository

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {


    suspend fun getImageOfTheDay(): PictureOfDay? {
        val json = AsteroidApi.retrofitService.getImageOfTheDayURL()
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<PictureOfDay> = moshi.adapter(PictureOfDay::class.java)

        return jsonAdapter.fromJson(json)
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val json = AsteroidApi.retrofitService.getAsteroids()
            val obj = JSONObject(json)
            val asteroids = parseAsteroidsJsonResult(obj)

            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }

}