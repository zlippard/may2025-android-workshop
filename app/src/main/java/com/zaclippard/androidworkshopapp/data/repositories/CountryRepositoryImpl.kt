package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.domain.Country

class CountryRepositoryImpl(
    private val service: CountryService,
) : CountryRepository {
    // In-memory cache
    private val countries = mutableListOf<Country>()

    override suspend fun fetchCountries(): Result<List<Country>> = runCatching {
        val countriesResponse = service.getAllCountries()

        if (countriesResponse.isSuccessful) {
            countries.clear()
            countries.addAll(countriesResponse.body() ?: emptyList())
            countries
        } else {
            throw Exception(countriesResponse.errorBody()?.string() ?: "Unknown error")
        }
    }

    override fun getCountry(index: Int): Country? = countries.getOrNull(index)
}
