package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

sealed interface CountryListIntent {
    data object Retry : CountryListIntent
    data object Refresh : CountryListIntent
}
