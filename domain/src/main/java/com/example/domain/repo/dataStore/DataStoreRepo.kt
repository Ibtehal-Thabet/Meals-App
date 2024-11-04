package com.example.domain.repo.dataStore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {

    suspend fun saveDarkThemeEnabled(enabled: Boolean)

    suspend fun getDarkThemeEnabled(): Flow<Boolean>
}