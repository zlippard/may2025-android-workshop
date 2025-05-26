package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.domain.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountryRepositoryImpl(
    private val service: CountryService,
) : CountryRepository {
    // In-memory cache
    private val _countryListResultStream = MutableStateFlow<Result<List<Country>>>(Result.success(emptyList()))

    override val countryListResultStream: Flow<Result<List<Country>>> = _countryListResultStream.asStateFlow()

    override suspend fun fetchCountries() {
        _countryListResultStream.value = runCatching {
            val countriesResponse = service.getAllCountries()

            if (countriesResponse.isSuccessful) {
                countriesResponse.body() ?: emptyList()
            } else {
                throw (Exception(countriesResponse.errorBody()?.string() ?: "Unknown error"))
            }
        }
    }

    override fun getCountry(index: Int): Country? {
        val cachedCountryListResult = _countryListResultStream.value
        return if (cachedCountryListResult.isSuccess) {
            val cachedCountries = cachedCountryListResult.getOrNull()
            cachedCountries?.getOrNull(index)
        } else { null }
    }
}
