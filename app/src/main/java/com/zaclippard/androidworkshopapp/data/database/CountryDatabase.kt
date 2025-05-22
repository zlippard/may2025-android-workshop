package com.zaclippard.androidworkshopapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zaclippard.androidworkshopapp.domain.Country

private const val DATABASE_NAME = "country_database"
private const val DATABASE_VERSION = 1

@Database(
    entities = [Country::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    companion object {
        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            CountryDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

