package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6_PRO
import app.cash.paparazzi.Paparazzi
import com.zaclippard.androidworkshopapp.countryList
import com.zaclippard.androidworkshopapp.presentation.ui.theme.AndroidWorkshopAppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CountryListScreenSnapshotTests {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_6_PRO,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun `snapshot`() {
        val testUiState = MutableStateFlow(CountryListUiState.Ready(countryList))
        val mockViewModel = mockk<CountryListViewModel> {
            every { uiState } returns testUiState
        }

        paparazzi.snapshot {
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
