package com.example.orangelastsession.ui.screens.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.Resources
import com.example.domain.entity.auth.AuthState
import com.example.domain.entity.auth.User
import com.example.domain.usecase.auth.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    var authState by mutableStateOf(AuthState())
        private set

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _user = MutableStateFlow<User>(User())
    val user = _user.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail = _userEmail.asStateFlow()

    var hasUser = mutableStateOf(false)
    var isLoading = mutableStateOf(false)

    init {
        if (hasUser.value){
            getUserInfo()
        }
        hasUser()
        getUserId()
    }

    fun onUserEmailChange(userEmail: String){
        authState = authState.copy(userEmail = userEmail)
    }

    fun onPasswordChange(password: String){
        authState = authState.copy(password = password)
    }

    fun onUserNameSignUpChange(userName: String){
        authState = authState.copy(userNameSignUp = userName)
    }

    fun onUserEmailSignUpChange(userEmail: String){
        authState = authState.copy(userEmailSignUp = userEmail)
    }

    fun onPasswordSignUpChange(password: String){
        authState = authState.copy(passwordSignUp = password)
    }

    fun onConfirmPasswordSignUpChange(confirmPassword: String){
        authState = authState.copy(confirmPasswordSignUp = confirmPassword)
    }

    private fun validateLoginForm() =
        authState.userEmail.isNotBlank() &&
                authState.password.isNotBlank()

    private fun validateSignupForm() =
        authState.userNameSignUp.isNotBlank() &&
                authState.userEmailSignUp.isNotBlank() &&
                authState.passwordSignUp.isNotBlank() &&
                authState.confirmPasswordSignUp.isNotBlank()


    fun getUserInfo(){
        viewModelScope.launch {
            getUserUseCase.getUserInfo().collect {
                when(it){
                    is Resources.Success -> {
                        isLoading.value = false
                        _user.value = it.data?:User()
                        _userName.value = it.data?.userName?:""
                        _userEmail.value = it.data?.userEmail?:""
                    }
                    is Resources.Error -> {
                        Log.i("User Info", it.message?:"Error")
                        isLoading.value = false
                    }
                    is Resources.Loading -> {
                        isLoading.value = true
                    }

                    else -> {}
                }
            }
        }
    }

    private fun hasUser(){
        viewModelScope.launch {
            getUserUseCase.hasUser().collect{
                Resources.Loading
                when(it){
                    is Resources.Success -> {
                        hasUser.value = it.data?:false
                        authState = authState.copy(isUserLoggedIn = true)
                    }
                    is Resources.Error -> {
                        Log.i("Login user", "User not found")
                        authState = authState.copy(signUpError = it.message)
                    }
                    is Resources.Loading -> {
//                            isLoading.value = false
                    }
                }
            }
        }
//        }catch (e: Exception) {
////            authState = authState.copy(signUpError = e.localizedMessage)
//            Log.i("auth vm", "error")
//            e.printStackTrace()
//        }
    }

    private fun getUserId() {
        viewModelScope.launch {
            getUserUseCase.getUserId().collect {
//                    Resources.Loading
                when (it) {
                    is Resources.Success -> {
                        _userId.value = it.data ?: ""
                    }

                    is Resources.Error -> {
                        Log.i("user id", it.message?:"User id not found")
                    }

                    is Resources.Loading -> {

                    }

                    else -> {}
                }
            }
        }
    }

    fun createUser(context: Context) {
            viewModelScope.launch {
                if (!validateSignupForm()) {
                    Toast.makeText(
                        context,
                        "Email and Password can not be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                if (authState.passwordSignUp != authState.confirmPasswordSignUp) {
                    Toast.makeText(context, "Password do not match", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                authState = authState.copy(isLoading = true)
                authState = authState.copy(signUpError = null)
                getUserUseCase.invoke(
                    authState.userNameSignUp,
                    authState.userEmailSignUp,
                    authState.passwordSignUp
                ).collect {
                    when (it) {
                        is Resources.Success -> {
                            Toast.makeText(context, "Success Signup", Toast.LENGTH_SHORT).show()
                            authState = authState.copy(isSuccessLogin = true)
                            authState = authState.copy(isLoading = false)
                            hasUser()
                        }

                        is Resources.Loading -> {
                            authState = authState.copy(isLoading = true)
                            isLoading.value = authState.isLoading
                        }

                        is Resources.Error -> {
                            Toast.makeText(context, it.message?:"Failed Signup", Toast.LENGTH_SHORT).show()
                            authState = authState.copy(isSuccessLogin = false)
                            authState = authState.copy(signUpError = it.message)
                            authState = authState.copy(isLoading = false)
                        }
                    }
                }
            }
    }

    fun loginUser(context: Context) {
            viewModelScope.launch {
                if (!validateLoginForm()){
                    Toast.makeText(context, "Email and Password can not be empty", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                authState = authState.copy(isLoading = true)
                authState = authState.copy(loginError = null)
                getUserUseCase.invokeLogin(
                    authState.userEmail,
                    authState.password
                ).collect{
                    when(it){
                        is Resources.Success -> {
                            _user.value = it.data?:User()
                            _userName.value = it.data?.userName?:""
                            _userEmail.value = it.data?.userEmail?:""
                            _userId.value = it.data?.userId?:"No user id"

                            hasUser()
                            getUserInfo()

                            authState = authState.copy(isSuccessLogin = true)
                            authState = authState.copy(isUserLoggedIn = true)
                            authState = authState.copy(isLoading = false)

                            Toast.makeText(context, "Success Login", Toast.LENGTH_SHORT).show()
                        }
                        is Resources.Loading -> {
                            authState = authState.copy(isLoading = true)
                            isLoading.value = authState.isLoading
                        }
                        is Resources.Error -> {
                            Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show()
                            authState = authState.copy(isSuccessLogin = false)
                            authState = authState.copy(loginError = it.message)
                            authState = authState.copy(isLoading = false)
                        }
                    }
                }
            }
    }

    fun logoutUser(context: Context) {
        viewModelScope.launch {
            getUserUseCase.logout().collect {
                when (it) {
                    is Resources.Success -> {
                        authState = authState.copy(isUserLoggedIn = false)
                        authState = authState.copy(isSuccessLogout = true)
                        isLoading.value = false
                        clearUserData()
                    }

                    is Resources.Error -> {
                        isLoading.value = false
                        Toast.makeText(context, it.message?:"Failed Logout, Please try later", Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        isLoading.value = true
                    }

                    else -> {}
                }
            }
        }
    }

    private fun clearUserData() {
        _userId.value = ""
        _userName.value = ""
        _userEmail.value = ""
        _user.value = User()
        authState = authState.copy(userEmail = "")
//        user = User()
    }

}