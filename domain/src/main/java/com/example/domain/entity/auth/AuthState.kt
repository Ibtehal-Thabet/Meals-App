package com.example.domain.entity.auth

data class AuthState(
    val userEmail: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val userEmailSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val isSuccessLogout: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null
)