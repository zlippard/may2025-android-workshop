package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepository
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListIntent.Favorite
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListIntent.Refresh
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListIntent.Retry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(
    val countryRepository: CountryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CountryListUiState>(CountryListUiState.Loading)

    val uiState: StateFlow<CountryListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            countryRepository.countryListResultStream
                .collect { countryListResult ->
                    countryListResult
                        .onSuccess {
                            _uiState.value = CountryListUiState.Ready(it)
                        }
                        .onFailure {
                            _uiState.value = CountryListUiState.Error("No countries to show: ${it.message}")
                        }
                }
        }

        fetchCountries()
    }

    fun handleIntent(intent: CountryListIntent) {
        when (intent) {
            is Retry -> fetchCountries()
            is Refresh -> fetchCountries(forceNetworkFetch = true)
            is Favorite -> markCountryAsFavorite(intent.country)
        }
    }

    private fun markCountryAsFavorite(country: Country) {
        viewModelScope.launch {
            countryRepository.markCountryAsFavorite(country)
        }
    }

    private fun fetchCountries(forceNetworkFetch: Boolean = false) {
        val state = _uiState.value
        _uiState.value = if (forceNetworkFetch && state is CountryListUiState.Ready) {
            CountryListUiState.Refreshing(state.countries)
        } else {
            CountryListUiState.Loading
        }

        viewModelScope.launch {
            countryRepository.fetchCountries(forceNetworkFetch)

            withContext(Dispatchers.Main) {
                (_uiState.value as? CountryListUiState.Refreshing)?.let { state ->
                    _uiState.value = CountryListUiState.Ready(state.countries)
                }
            }
        }
    }
}
