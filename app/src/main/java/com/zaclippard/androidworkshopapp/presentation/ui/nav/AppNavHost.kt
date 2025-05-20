package com.zaclippard.androidworkshopapp.presentation.ui.nav

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.squareup.moshi.Moshi
import com.zaclippard.androidworkshopapp.domain.Country
import com.zaclippard.androidworkshopapp.presentation.ui.screens.about.AboutScreen
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.CountryDetailsScreen
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = ScreenRoute.CountryList.route) {
        composable(ScreenRoute.CountryList.route) {
            CountryListScreen(
                onCountryClick = { country ->
                    val countryJson = Uri.encode(
                        Moshi.Builder()
                            .build()
                            .adapter(Country::class.java)
                            .toJson(country)
                    )
                    navController.navigate(ScreenRoute.CountryDetails.createRoute(countryJson))
                },
                onAboutClick = {
                    navController.navigate(ScreenRoute.About.path)
                },
            )
        }

        composable(
            ScreenRoute.CountryDetails.route,
            arguments = listOf(
                navArgument(ScreenRoute.CountryDetails.COUNTRY_PARAM) {
                    type = CountryArgType()
                }
            )
        ) { backStackEntry ->
            val country = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable(ScreenRoute.CountryDetails.COUNTRY_PARAM, Country::class.java)
            } else {
                backStackEntry.arguments?.getParcelable<Country>(ScreenRoute.CountryDetails.COUNTRY_PARAM)
            } ?: throw Exception("No arguments found for Country Details screen!")
            CountryDetailsScreen(country) {
                navController.navigateUp()
            }
        }

        composable(ScreenRoute.About.path) {
            AboutScreen { navController.navigateUp() }
        }
    }
}

private class CountryArgType : NavType<Country>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Country? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Country::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Country {
        val moshi = Moshi.Builder().build()
        return moshi.adapter(Country::class.java).fromJson(value)!!
    }

    override fun put(bundle: Bundle, key: String, value: Country) {
        bundle.putParcelable(key, value)
    }
}
