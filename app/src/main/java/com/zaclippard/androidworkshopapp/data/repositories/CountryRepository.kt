package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.domain.Country

interface CountryRepository {
    suspend fun fetchCountries(): Result<List<Country>>
    fun getCountry(index: Int): Country?
}
