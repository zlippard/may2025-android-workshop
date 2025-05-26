package com.zaclippard.androidworkshopapp.data.prefs

import kotlinx.coroutines.flow.Flow

interface AndroidWorkshopPrefs {
    val localStorageEnabledStream: Flow<Boolean>
    val rotationEnabledStream: Flow<Boolean>

    suspend fun toggleLocalStorage()
    suspend fun toggleRotation()
}
