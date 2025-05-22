package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import com.zaclippard.androidworkshopapp.domain.Country

sealed interface CountryListUiState {
    data object Loading : CountryListUiState

    data class Ready(val countries: List<Country>) : CountryListUiState

    data class Refreshing(val countries: List<Country>) : CountryListUiState

    data class Error(val message: String) : CountryListUiState
}
