package com.zaclippard.androidworkshopapp.presentation.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zaclippard.androidworkshopapp.presentation.ui.screens.about.AboutScreen
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.CountryDetailsScreen
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListScreen
import com.zaclippard.androidworkshopapp.presentation.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = ScreenRoute.CountryList.route) {
        composable(ScreenRoute.CountryList.route) {
            CountryListScreen(
                onCountryClick = { countryIndex ->
                    navController.navigate(ScreenRoute.CountryDetails.createRoute(countryIndex))
                },
                onAboutClick = {
                    navController.navigate(ScreenRoute.About.createRoute("Zac"))
                },
                onSettingsClick = {
                    navController.navigate(ScreenRoute.Settings.route)
                }
            )
        }

        composable(
            route = ScreenRoute.CountryDetails.route,
            arguments = listOf(
                navArgument(ScreenRoute.CountryDetails.COUNTRY_INDEX_PARAM) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val countryIndex = backStackEntry.arguments?.getInt(ScreenRoute.CountryDetails.COUNTRY_INDEX_PARAM)
                ?: throw Exception("No argument found for Country Details screen!")
            CountryDetailsScreen(countryIndex) {
                navController.navigateUp()
            }
        }

        composable(
            route = ScreenRoute.About.route,
            arguments = listOf(
                navArgument(ScreenRoute.About.NAME_PARAM) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString(ScreenRoute.About.NAME_PARAM)
                ?: throw Exception("Name is required!")
            AboutScreen(name) { navController.navigateUp() }
        }

        composable(ScreenRoute.Settings.route) {
            SettingsScreen {
                navController.navigateUp()
            }
        }
    }
}
