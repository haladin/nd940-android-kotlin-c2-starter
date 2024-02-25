package com.udacity.asteroidradar.api

import android.util.Log
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.utils.Constants.BASE_URL
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.utils.daysToGet
import com.udacity.asteroidradar.utils.getTodayFormatted
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
private val httpClient = OkHttpClient.Builder().addInterceptor(logger).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create()) // -> Converts response to string
    .client(httpClient)
    //.addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String = getTodayFormatted(),
        @Query("end_date") endDate: String = daysToGet(DEFAULT_END_DATE_DAYS),
        @Query("api_key") apiKey: String = API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDayURL(
        @Query("api_key") apiKey: String = API_KEY
    ): String
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}