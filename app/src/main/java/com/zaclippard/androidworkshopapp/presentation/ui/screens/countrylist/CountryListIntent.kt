package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import com.zaclippard.androidworkshopapp.domain.Country

sealed interface CountryListIntent {
    data object Retry : CountryListIntent
    data object Refresh : CountryListIntent
    data class Favorite(val country: Country) : CountryListIntent
}
