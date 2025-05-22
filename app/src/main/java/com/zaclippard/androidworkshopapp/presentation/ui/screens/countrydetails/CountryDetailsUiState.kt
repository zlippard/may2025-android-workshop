package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails

import com.zaclippard.androidworkshopapp.domain.Country

sealed interface CountryDetailsUiState {
    val title: String

    data class Ready(
        val country: Country,
        override val title: String = country.name,
    ) : CountryDetailsUiState

    data class Error(
        val message: String,
        override val title: String = "Error",
    ) : CountryDetailsUiState
}
