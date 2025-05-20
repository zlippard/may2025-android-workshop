package com.zaclippard.androidworkshopapp.presentation.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.CountryDetailsScreen
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var selectedCountry: Country? = null

    NavHost(navController, startDestination = ScreenRoute.CountryList.path) {
        composable(ScreenRoute.CountryList.path) {
            CountryListScreen { country ->
                selectedCountry = country
                navController.navigate(ScreenRoute.CountryDetails.path)
            }
        }

        composable(ScreenRoute.CountryDetails.path) {
            CountryDetailsScreen(selectedCountry!!) {
                navController.navigateUp()
            }
        }
    }
}
