package com.zaclippard.androidworkshopapp.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.COUNTRY_DETAILS_AREA_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.COUNTRY_DETAILS_CAPITAL_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.COUNTRY_DETAILS_POPULATION_TAG

class CountryDetailsRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
) : Robot {

    private val capitalText = composeTestRule.onNodeWithTag(COUNTRY_DETAILS_CAPITAL_TAG)
    private val populationText = composeTestRule.onNodeWithTag(COUNTRY_DETAILS_POPULATION_TAG)
    private val areaText = composeTestRule.onNodeWithTag(COUNTRY_DETAILS_AREA_TAG)

    fun verifyUi(
        capital: String,
        population: String,
        area: String,
    ) {
        capitalText
            .assertIsDisplayed()
            .assertTextEquals("Capital: $capital")

        populationText
            .assertIsDisplayed()
            .assertTextEquals("Population: $population")

        areaText
            .assertIsDisplayed()
            .assertTextEquals("Area: $area")
    }

    infix fun backToCountryList(block: CountryListRobot.() -> Unit): CountryListRobot {
//        CountryListRobot(composeTestRule).apply { block() }
        return navigateBackToPrevRobot(composeTestRule, block)
    }
}
