package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.domain.Country
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountryRepositoryImpl(
    private val service: CountryService,
) : CountryRepository {
    private val _countryListResultStream: MutableStateFlow<Result<List<Country>>> = MutableStateFlow(Result.success(emptyList()))
    override val countryListResultStream: StateFlow<Result<List<Country>>> = _countryListResultStream.asStateFlow()

    override suspend fun fetchCountries() {
        _countryListResultStream.value = runCatching {
            val countriesResponse = service.getAllCountries()

            if (countriesResponse.isSuccessful) {
                countriesResponse.body() ?: emptyList()
            } else {
                throw Exception(countriesResponse.errorBody()?.string() ?: "Unknown error")
            }
        }
    }

    override fun getCountry(index: Int): Country? =
        _countryListResultStream.value.getOrNull()?.get(index)
}
