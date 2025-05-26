package com.zaclippard.androidworkshopapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zaclippard.androidworkshopapp.domain.Country

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCountries(vararg country: Country)

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries(): List<Country>

    @Update
    suspend fun updateCountry(country: Country)

    @Query("DELETE FROM countries")
    suspend fun deleteAllCountries()
}
