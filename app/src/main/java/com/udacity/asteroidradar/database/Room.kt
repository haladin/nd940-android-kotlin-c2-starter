package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("select * from asteroid_table ORDER BY closeApproachDate ")
    fun getAllAsteroids(): Flow<List<DatabaseAsteroid>>

    @Query("select * from asteroid_table WHERE closeApproachDate == :timestamp ORDER BY closeApproachDate ")
    fun getTodayAsteroids(timestamp: Long): Flow<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :startDay AND closeApproachDate <= :endDay ORDER BY closeApproachDate")
    fun getWeekAsteroids(startDay: Long, endDay: Long): Flow<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :timestamp")
    fun deleteOldAsteroids(timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroidsDB"
                )
                    //.addMigrations(MIGRATION_1_2)
                    .build()
        }
    }
    return INSTANCE
}

val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE asteroid_table;")
    }
}