package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.domain.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    val countryListResultStream: Flow<Result<List<Country>>>

    suspend fun fetchCountries()
    fun getCountry(index: Int): Country?
}
