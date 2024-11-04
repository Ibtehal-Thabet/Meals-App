package com.example.data.repo.dataStore

import com.example.domain.repo.dataStore.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepoImpl @Inject constructor(private val dataStore: androidx.datastore.core.DataStore<Preferences>): DataStoreRepo{

    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_enabled")
    override suspend fun saveDarkThemeEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = enabled
        }
    }

    override suspend fun getDarkThemeEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }
    }

}
