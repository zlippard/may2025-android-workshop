package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.domain.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    val countryListStream: Flow<List<Country>>
    suspend fun fetchCountries()
    fun getCountry(index: Int): Country?
}
