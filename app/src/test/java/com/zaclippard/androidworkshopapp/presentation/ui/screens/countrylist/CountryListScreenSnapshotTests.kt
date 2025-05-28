package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import com.zaclippard.androidworkshopapp.countryList
import com.zaclippard.androidworkshopapp.presentation.ui.theme.AndroidWorkshopAppTheme
import com.zaclippard.androidworkshopapp.rules.SnapshotTestRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CountryListScreenSnapshotTests {

    @get:Rule
    val snapshotTestRule = SnapshotTestRule()

    @Test
    fun snapshot() {
        val testUiState = MutableStateFlow(CountryListUiState.Ready(countryList))
        val mockViewModel = mockk<CountryListViewModel> {
            every { uiState } returns testUiState
        }

        snapshotTestRule.snapshot {
            AndroidWorkshopAppTheme {
                CountryListScreen(
                    viewModel = mockViewModel,
                    onCountryClick = {},
                    onSettingsClick = {},
                    onAboutClick = {}
                )
            }
        }
    }
}
