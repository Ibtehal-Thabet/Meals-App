package com.example.orangelastsession.ui.screens.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.Resources
import com.example.domain.entity.auth.User
import com.example.domain.usecase.auth.GetUserUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.domain.usecase.dataStore.GetDataStoreUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDataStoreUseCase: GetDataStoreUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private val _userId = MutableStateFlow<String>("")
    val userId = _userId.asStateFlow()

    private val _deleteAccountStatus = MutableStateFlow<Unit>(Unit)
    val deleteAccountStatus = _deleteAccountStatus.asStateFlow()

    var isLoading = mutableStateOf(false)

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    val argsUser: String? = savedStateHandle["user"]
    lateinit var user: User

    init {
        argsUser?.let {
            user = Gson().fromJson(it, User::class.java)
        }

        getDarkTheme()
    }

    fun toggleTheme() {
        viewModelScope.launch {
            getDataStoreUseCase.invoke(!_isDarkTheme.value)
        }
    }

    private fun getDarkTheme() {
        viewModelScope.launch {
//            viewModelScope.launch {
//                getDataStoreUseCase.invoke().collect {
//                    _isDarkTheme.value = it
//                }
//            }
            getDataStoreUseCase.invoke().onEach { isDark ->
                _isDarkTheme.value = isDark
            }.launchIn(viewModelScope)
        }
    }

    fun updateUserName(context: Context, newName: String) {
        viewModelScope.launch {
            isLoading.value = true
            getUserUseCase.updateUserName(newName).collect{
                when (it) {
                    is Resources.Success -> {
                        isLoading.value = false
                        Toast.makeText(context, "Name Updated Successfully", Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Error -> {
                        isLoading.value = false
                        Toast.makeText(context, it.message?:"Failed Updated Name, Please try later", Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        isLoading.value = true
                    }

                    else -> {}
                }
            }
        }
    }

    fun changePassword(context: Context, currentPassword: String, newPass: String, confirmNewPass: String) {
        viewModelScope.launch {
            isLoading.value = true
            if (newPass != confirmNewPass) {
                Toast.makeText(context, "Password do not match", Toast.LENGTH_SHORT).show()
                isLoading.value = false
                return@launch
            }
            if (currentPassword == newPass) {
                Toast.makeText(context, "Please Enter New Password", Toast.LENGTH_SHORT).show()
                isLoading.value = false
                return@launch
            }
            getUserUseCase.updatePassword(currentPassword, newPass, confirmNewPass).collect{
                when (it) {
                    is Resources.Success -> {
                        isLoading.value = false
                        Toast.makeText(context, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Error -> {
                        isLoading.value = false
                        Toast.makeText(context, it.message?:"Failed Changed Password, Please try later", Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        isLoading.value = true
                    }

                    else -> {}
                }
            }
        }
    }

    fun deleteUserAccount(context: Context) {
        viewModelScope.launch {
            getUserUseCase.deleteUserAccount().collect {
                when (it) {
                    is Resources.Success -> {
                        isLoading.value = false
                        _deleteAccountStatus.value = it.data!!
                    }

                    is Resources.Error -> {
                        isLoading.value = false
                        Toast.makeText(
                            context,
                            it.message ?: "Failed Delete User, Please try later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resources.Loading -> {
                        isLoading.value = true
                    }

                    else -> {}
                }
            }
        }
    }
}