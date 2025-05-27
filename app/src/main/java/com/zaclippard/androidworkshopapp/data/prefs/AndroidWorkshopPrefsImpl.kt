package com.zaclippard.androidworkshopapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AndroidWorkshopPrefsImpl @Inject constructor(
    @ApplicationContext context: Context,
) : AndroidWorkshopPrefs {
    private val Context.dataStore by preferencesDataStore(name = STORE_NAME)
    private val dataStore = context.dataStore

    override val localStorageEnabledStream: Flow<Boolean> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        it[STORE_KEY_LOCAL_STORAGE] != false
    }

    override val rotationEnabledStream: Flow<Boolean> = dataStore.data.catch {
        emit(emptyPreferences())
    }.map {
        it[STORE_KEY_ROTATION] != false
    }

    override suspend fun toggleLocalStorage() {
        dataStore.edit {
            it[STORE_KEY_LOCAL_STORAGE] = it[STORE_KEY_LOCAL_STORAGE]?.not() == true
        }
    }

    override suspend fun toggleRotation() {
        dataStore.edit {
            it[STORE_KEY_ROTATION] = it[STORE_KEY_ROTATION]?.not() == true
        }
    }

    companion object {
        private const val STORE_NAME = "country_prefs"
        private val STORE_KEY_LOCAL_STORAGE = booleanPreferencesKey("local_storage")
        private val STORE_KEY_ROTATION = booleanPreferencesKey("rotation")
    }
}
