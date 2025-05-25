package com.zaclippard.androidworkshopapp.data.repositories

import android.util.Log
import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.domain.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountryRepositoryImpl(
    private val service: CountryService,
) : CountryRepository {
    // In-memory cache
    private val _countryListStream = MutableStateFlow<List<Country>>(emptyList())

    override val countryListStream: Flow<List<Country>> = _countryListStream.asStateFlow()

    override suspend fun fetchCountries() {
        runCatching {
            val countriesResponse = service.getAllCountries()

            if (countriesResponse.isSuccessful) {
                countriesResponse.body() ?: emptyList()
            } else {
                val message = countriesResponse.errorBody()?.string() ?: "Unknown error"
                Log.e("WORKSHOP", message)
                _countryListStream.value
            }
        }.onSuccess {
            _countryListStream.value = it
        }.onFailure {
            Log.e("WORKSHOP", it.message ?: "Unknown error")
        }
    }

    override fun getCountry(index: Int): Country? = _countryListStream.value.getOrNull(index)
}
