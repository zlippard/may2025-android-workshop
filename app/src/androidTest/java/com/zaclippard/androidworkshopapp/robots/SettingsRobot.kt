package com.zaclippard.androidworkshopapp.robots

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zaclippard.androidworkshopapp.presentation.ui.screens.settings.ENABLE_LOCAL_STORAGE_TOGGLE_TAG
import com.zaclippard.androidworkshopapp.presentation.ui.screens.settings.ENABLE_ROTATION_TOGGLE_TAG

class SettingsRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
) : Robot {

    private val localStorageToggleRow = composeTestRule.onNodeWithTag(ENABLE_LOCAL_STORAGE_TOGGLE_TAG)
    private val rotationToggleRow = composeTestRule.onNodeWithTag(ENABLE_ROTATION_TOGGLE_TAG)

    fun verifyUi() {
        localStorageToggleRow
            .assertIsDisplayed()
        rotationToggleRow
            .assertIsDisplayed()
    }

    infix fun backToCountryList(block: CountryListRobot.() -> Unit): CountryListRobot {
//        return CountryListRobot(composeTestRule).apply { block() }
        return navigateBackToPrevRobot(composeTestRule, block)
    }
}
