package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.convertLongToDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "asteroid_table")
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: Long,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

//@Entity(tableName = "image_of_the_day")
//data class DatabaseImage(
//    @PrimaryKey
//    val url: String,
//    val media_type: String,
//    val title: String,
//)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map { asteroid ->
        Asteroid(
            id = asteroid.id,
            codename = asteroid.codename,
            closeApproachDate = convertLongToDate(asteroid.closeApproachDate),
            absoluteMagnitude = asteroid.absoluteMagnitude,
            estimatedDiameter = asteroid.estimatedDiameter,
            relativeVelocity = asteroid.relativeVelocity,
            distanceFromEarth = asteroid.distanceFromEarth,
            isPotentiallyHazardous = asteroid.isPotentiallyHazardous
        )
    }
}
