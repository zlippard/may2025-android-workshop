package com.zaclippard.androidworkshopapp.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.COUNTRY_LIST_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.COUNTRY_ROW_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.SETTINGS_ICON_TAG

class CountryListRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
) : Robot {

    private val countryList = composeTestRule.onNodeWithTag(COUNTRY_LIST_TAG)
    private val firstCountryRow = composeTestRule.onAllNodesWithTag(COUNTRY_ROW_TAG)
        .onFirst()
    private val settingsIcon = composeTestRule.onNodeWithTag(SETTINGS_ICON_TAG)

    fun verifyUi() {
        countryList.assertIsDisplayed()
    }

    infix fun goToCountryDetails(block: CountryDetailsRobot.() -> Unit): CountryDetailsRobot {
//        firstCountryRow.performTap()
//        return CountryDetailsRobot(composeTestRule).apply { block() }
        return navigateToRobot(composeTestRule, firstCountryRow, block)
    }

    infix fun goToSettings(block: SettingsRobot.() -> Unit): SettingsRobot {
//        settingsIcon.performTap()
//        return SettingsRobot(composeTestRule).apply { block() }
        return navigateToRobot(composeTestRule, settingsIcon, block)
    }
}
