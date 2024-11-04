package com.example.domain.usecase.auth

import com.example.domain.repo.auth.AuthRepo

class GetUserUseCase(private val authRepo: AuthRepo) {
    suspend operator fun invoke(name: String, email: String, password: String) = authRepo.createUser(name, email, password)

    suspend fun invokeLogin(email: String, password: String) = authRepo.login(email, password)

    suspend fun getUserInfo() = authRepo.getUserInfo()

    suspend fun updateUserName(name: String) = authRepo.updateUserName(name)

    suspend fun updatePassword(currentPassword: String, newPassword: String, confirmNewPassword: String) = authRepo.updatePassword(currentPassword, newPassword, confirmNewPassword)

    suspend fun logout() = authRepo.logout()

    suspend fun deleteUserAccount() = authRepo.deleteUserAccount()

    suspend fun hasUser() = authRepo.hasUser()

    suspend fun getUserId() = authRepo.getUserId()
}