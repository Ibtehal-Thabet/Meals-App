package com.example.domain.repo.network

import kotlinx.coroutines.flow.Flow

interface NetworkRepo {

    fun getNetworkStatus(): Flow<Boolean>
}