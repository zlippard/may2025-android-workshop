package com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zaclippard.androidworkshopapp.AndroidWorkshopApp
import com.zaclippard.androidworkshopapp.data.repositories.CountryRepository
import com.zaclippard.androidworkshopapp.presentation.ui.screens.countrylist.CountryListIntent.Retry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountryListViewModel(
    val countryRepository: CountryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CountryListUiState>(CountryListUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            countryRepository.countryListStream
                .collect { newState ->
                    _uiState.value = if (newState.isNotEmpty()) {
                        CountryListUiState.Ready(newState)
                    } else {
                        CountryListUiState.Error("No countries to show.")
                    }
                }
        }

        fetchCountries()
    }

    fun handleIntent(intent: CountryListIntent) {
        when (intent) {
            is Retry -> fetchCountries()
        }
    }

    private fun fetchCountries() {
        _uiState.value = CountryListUiState.Loading

        viewModelScope.launch {
            countryRepository.fetchCountries()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val app = checkNotNull(extras[APPLICATION_KEY]) as AndroidWorkshopApp
                return CountryListViewModel(
                    app.countryRepository,
                ) as T
            }
        }
    }
}
