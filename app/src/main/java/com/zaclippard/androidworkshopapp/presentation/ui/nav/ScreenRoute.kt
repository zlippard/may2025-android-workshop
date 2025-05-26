package com.zaclippard.androidworkshopapp.presentation.ui.nav

sealed interface ScreenRoute {
    val route: String

    data object CountryList : ScreenRoute {
        override val route = "country-list"
    }

    data object CountryDetails : ScreenRoute {
        const val COUNTRY_INDEX_PARAM = "countryIndex"

        private val path = "country-details"

        override val route = "$path/{$COUNTRY_INDEX_PARAM}"

        fun createRoute(countryIndex: Int): String {
            return "$path/$countryIndex"
        }
    }

    data object About : ScreenRoute {
        const val NAME_PARAM = "name"
        private val path = "about"

        override val route = "$path/{$NAME_PARAM}"

        fun createRoute(name: String): String {
            return "$path/$name"
        }
    }

    data object Settings : ScreenRoute {
        override val route = "settings"
    }
}
