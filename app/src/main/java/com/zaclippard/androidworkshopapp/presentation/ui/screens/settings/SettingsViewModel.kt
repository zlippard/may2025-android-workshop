package com.zaclippard.androidworkshopapp.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.zaclippard.androidworkshopapp.AndroidWorkshopApp
import com.zaclippard.androidworkshopapp.data.prefs.AndroidWorkshopPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefs: AndroidWorkshopPrefs) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.localStorageEnabledStream
                .collect {
                    _uiState.value = _uiState.value.copy(localStorageEnabled = it)
                }
        }

        viewModelScope.launch {
            prefs.rotationEnabledStream
                .collect {
                    _uiState.value = _uiState.value.copy(rotationEnabled = it)
                }
        }
    }

    fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.TOGGLE_LOCAL_STORAGE -> toggleLocalStorage()
            SettingsIntent.TOGGLE_ROTATION -> toggleRotation()
        }
    }

    private fun toggleLocalStorage() {
        viewModelScope.launch {
            prefs.toggleLocalStorage()
        }
    }

    private fun toggleRotation() {
        viewModelScope.launch {
            prefs.toggleRotation()
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
                return SettingsViewModel(
                    app.prefs,
                ) as T
            }
        }
    }
}
