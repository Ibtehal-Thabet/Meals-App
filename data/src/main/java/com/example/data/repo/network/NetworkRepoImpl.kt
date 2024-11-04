package com.example.data.repo.network

import com.example.data.network.NetworkStatusTracker
import com.example.domain.repo.network.NetworkRepo
import kotlinx.coroutines.flow.Flow

class NetworkRepoImpl(private val networkStatusTracker: NetworkStatusTracker) : NetworkRepo {

    override fun getNetworkStatus(): Flow<Boolean> = networkStatusTracker.getNetworkStatus()
}