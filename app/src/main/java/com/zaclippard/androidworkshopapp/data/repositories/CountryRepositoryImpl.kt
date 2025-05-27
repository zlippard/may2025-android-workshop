package com.zaclippard.androidworkshopapp.data.repositories

import com.zaclippard.androidworkshopapp.data.database.CountryDao
import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.data.prefs.AndroidWorkshopPrefs
import com.zaclippard.androidworkshopapp.domain.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val service: CountryService,
    private val countryDao: CountryDao,
    private val prefs: AndroidWorkshopPrefs,
) : CountryRepository {
    // In-memory cache
    private val _countryListResultStream = MutableStateFlow<Result<List<Country>>>(Result.success(emptyList()))

    override val countryListResultStream: Flow<Result<List<Country>>> = _countryListResultStream.asStateFlow()

    override suspend fun fetchCountries(forceNetworkFetch: Boolean) {
        val isLocalStorageEnabled = prefs.localStorageEnabledStream.first()

        _countryListResultStream.value = runCatching {
            val countriesFromDb = if (isLocalStorageEnabled) {
                countryDao.getAllCountries()
            } else {
                emptyList()
            }

            if (forceNetworkFetch || countriesFromDb.isEmpty()) {
                val countriesResponse = service.getAllCountries()

                if (countriesResponse.isSuccessful) {
                    val newCountries = countriesResponse.body() ?: emptyList()
                    if (isLocalStorageEnabled) {
                        countryDao.addCountries(*newCountries.toTypedArray())
                        countryDao.getAllCountries()
                    } else {
                        newCountries
                    }
                } else {
                    throw (Exception(countriesResponse.errorBody()?.string() ?: "Unknown error"))
                }
            } else {
                countriesFromDb
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

    override suspend fun markCountryAsFavorite(country: Country) {
        val isLocalStorageEnabled = prefs.localStorageEnabledStream.first()

        _countryListResultStream.value.getOrNull()?.let {
            val countries = it.toMutableList()
            val countryIndex = _countryListResultStream.value.getOrNull()?.indexOf(country) ?: -1
            if (countryIndex < 0) {
                return
            }

            val updatedCountry = country.copy(isFavorite = country.isFavorite.not())
            countries[countryIndex] = updatedCountry

            _countryListResultStream.value = if (isLocalStorageEnabled) {
                countryDao.updateCountry(updatedCountry)
                Result.success(countryDao.getAllCountries())
            } else {
                Result.success(countries)
            }
        }
    }

}
