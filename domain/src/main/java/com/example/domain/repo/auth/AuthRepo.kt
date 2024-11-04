package com.example.domain.repo.auth

import com.example.domain.common.Resources
import com.example.domain.entity.auth.User
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    suspend fun createUser(name: String,
                           email: String,
                           password: String): Flow<Resources<User>>

    suspend fun login(
        email: String,
        password: String): Flow<Resources<User>>

    suspend fun getUserInfo(): Flow<Resources<User?>>

//    suspend fun listenToUserInfoUpdates(): Flow<Resources<User>>

    suspend fun updateUserName(name: String): Flow<Resources<User>>

    suspend fun updatePassword(currentPassword: String, newPassword: String, confirmNewPassword: String): Flow<Resources<User>>

    suspend fun logout(): Flow<Resources<User>>

    suspend fun deleteUserAccount(): Flow<Resources<Unit>>

    suspend fun hasUser(): Flow<Resources<Boolean>>

    suspend fun getUserId(): Flow<Resources<String>>
}