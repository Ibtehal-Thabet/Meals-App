package com.example.orangelastsession.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repo.network.NetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    networkRepository: NetworkRepo
) : ViewModel() {
    val isNetworkAvailable: StateFlow<Boolean> = networkRepository.getNetworkStatus()
        .stateIn(viewModelScope, SharingStarted.Lazily, true)
}