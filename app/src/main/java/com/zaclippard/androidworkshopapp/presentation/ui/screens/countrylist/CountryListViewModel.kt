package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.zaclippard.androidworkshopapp.data.network.CountryService
import com.zaclippard.androidworkshopapp.data.network.adapters.CountryAdapter
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListIntent.Retry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CountryListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CountryListUiState>(CountryListUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        fetchCountries()
    }

    fun handleIntent(intent: CountryListIntent) {
        when (intent) {
            is Retry -> fetchCountries()
        }
    }

    private fun fetchCountries() {
        _uiState.value = CountryListUiState.Loading

        viewModelScope.launch {
            try {
                val moshi = Moshi.Builder()
                    .add(CountryAdapter())
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://restcountries.com/")
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()


                _uiState.value = retrofit
                    .create(CountryService::class.java)
                    .getAllCountries()
                    .body()?.let { countries ->
                        CountryListUiState.Ready(countries)
                    } ?: run {
                    CountryListUiState.Error("No countries returned")
                }
            } catch (e: Exception) {
                _uiState.value = CountryListUiState.Error("Uh-oh something went wrong: ${e.message}")
            }
        }
    }

}
