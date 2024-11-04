package com.example.domain.usecase.dataStore

import com.example.domain.repo.dataStore.DataStoreRepo

class GetDataStoreUseCase(private val dataStoreRepo: DataStoreRepo) {
    suspend operator fun invoke(enabled: Boolean) = dataStoreRepo.saveDarkThemeEnabled(enabled)

    suspend operator fun invoke() = dataStoreRepo.getDarkThemeEnabled()

}