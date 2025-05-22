package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.zaclippard.androidworkshopapp.AndroidWorkshopApp
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountryDetailsViewModel(
    countryIndex: Int,
    countryRepository: CountryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CountryDetailsUiState>(
        countryRepository.getCountry(countryIndex)?.let { country ->
            CountryDetailsUiState.Ready(country)
        } ?: CountryDetailsUiState.Error("No country found for countryIndex: $countryIndex")
    )

    val uiState = _uiState.asStateFlow()

    companion object {
        class Factory(val countryIndex: Int) : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val app = checkNotNull(extras[APPLICATION_KEY]) as AndroidWorkshopApp
                return CountryDetailsViewModel(
                    countryIndex,
                    app.countryRepository,
                ) as T
            }
        }

        fun createFactory(countryIndex: Int) = Factory(countryIndex)
    }
}
