package com.zaclippard.androidworkshopapp.tests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso.pressBack
import com.zaclippard.androidworkshopapp.performTap
import com.zaclippard.androidworkshopapp.presentation.ui.MainActivity
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.COUNTRY_DETAILS_AREA_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.COUNTRY_DETAILS_CAPITAL_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails.COUNTRY_DETAILS_POPULATION_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.COUNTRY_LIST_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.COUNTRY_ROW_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.SETTINGS_ICON_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.settings.ENABLE_LOCAL_STORAGE_TOGGLE_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.settings.ENABLE_ROTATION_TOGGLE_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.settings.TOGGLE_SWITCH_TAG
import com.zaclippard.androidworkshopapp.robots.CountryListRobot
import com.zaclippard.androidworkshopapp.robots.robot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class AndroidWorkshopTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun viewCountryListAndDetails() {
        // Country List Screen
        val countryList = composeTestRule.onNodeWithTag(COUNTRY_LIST_TAG)
        val firstCountryRow = composeTestRule.onAllNodesWithTag(COUNTRY_ROW_TAG)
            .onFirst()

        countryList.assertIsDisplayed()

        composeTestRule.onNodeWithTag(SETTINGS_ICON_TAG).assertIsDisplayed().performTap()

        // Settings Screen
        composeTestRule.onNodeWithTag(ENABLE_LOCAL_STORAGE_TOGGLE_TAG)
            .assertIsDisplayed()
            .onChildren()
            .filterToOne(hasTestTag(TOGGLE_SWITCH_TAG))
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(ENABLE_ROTATION_TOGGLE_TAG)
            .assertIsDisplayed()
            .onChildren()
            .filterToOne(hasTestTag(TOGGLE_SWITCH_TAG))
            .assertIsDisplayed()

        pressBack()

        // Country List Screen
        firstCountryRow
            .performTap()

        // Country Details Screen
        composeTestRule.onNodeWithTag(COUNTRY_DETAILS_CAPITAL_TAG)
            .assertIsDisplayed()
            .assertTextEquals("Capital: Washington DC")
        composeTestRule.onNodeWithTag(COUNTRY_DETAILS_POPULATION_TAG)
            .assertIsDisplayed()
            .assertTextEquals("Population: 1000000")
        composeTestRule.onNodeWithTag(COUNTRY_DETAILS_AREA_TAG)
            .assertIsDisplayed()
            .assertTextEquals("Area: 1000000.0")
        pressBack()

        // Country List Screen
        countryList.assertIsDisplayed()
    }

    @Test
    fun viewCountryListAndDetailsWithRobotPattern() {
//        CountryListRobot(composeTestRule as AndroidComposeTestRule<ActivityScenarioRule<*>, *>).apply {
        robot<CountryListRobot>(composeTestRule) {
            verifyUi()
        } goToSettings {
            verifyUi()
        } backToCountryList {
            verifyUi()
        } goToCountryDetails {
            verifyUi(
                capital = "Washington DC",
                population = "1000000",
                area = "1000000.0",
            )
        } backToCountryList {
            verifyUi()
        }
    }
}
