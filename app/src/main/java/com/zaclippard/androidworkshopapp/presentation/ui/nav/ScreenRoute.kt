package com.zaclippard.androidworkshopapp.presentation.ui.nav

sealed interface ScreenRoute {
    val route: String

    data object CountryList : ScreenRoute {
        override val route = "country-list"
    }

    data object CountryDetails : ScreenRoute {
        const val COUNTRY_PARAM = "country"

        private val path = "country-details"

        override val route = "$path/{$COUNTRY_PARAM}"

        fun createRoute(countryJson: String): String {
            return "$path/$countryJson"
        }
    }

    data object About : ScreenRoute {
        override val path = "about"
    }
}
