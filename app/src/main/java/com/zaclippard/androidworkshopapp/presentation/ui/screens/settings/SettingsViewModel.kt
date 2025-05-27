package com.zaclippard.androidworkshopapp.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaclippard.androidworkshopapp.data.prefs.AndroidWorkshopPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: AndroidWorkshopPrefs) : ViewModel() {
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
}
