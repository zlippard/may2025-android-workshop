package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrydetails

import androidx.lifecycle.ViewModel
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = CountryDetailsViewModel.Factory::class)
class CountryDetailsViewModel @AssistedInject constructor(
    @Assisted countryIndex: Int,
    countryRepository: CountryRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(countryIndex: Int): CountryDetailsViewModel
    }

    private val _uiState = MutableStateFlow<CountryDetailsUiState>(
        countryRepository.getCountry(countryIndex)?.let { country ->
            CountryDetailsUiState.Ready(country)
        } ?: CountryDetailsUiState.Error("No country found for countryIndex: $countryIndex")
    )

    val uiState = _uiState.asStateFlow()
}
