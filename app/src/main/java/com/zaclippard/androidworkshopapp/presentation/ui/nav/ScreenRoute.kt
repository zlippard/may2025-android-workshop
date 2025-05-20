package com.zaclippard.androidworkshopapp.presentation.ui.nav

sealed interface ScreenRoute {
    val path: String

    data object CountryList : ScreenRoute {
        override val path = "country-list"
    }

    data object CountryDetails : ScreenRoute {
        override val path = "country-details"
    }
}
