package com.example.data.repo.auth

import android.util.Log
import com.example.data.repo.storage.USERS_COLLECTION_REF
import com.example.domain.common.Resources
import com.example.domain.entity.auth.User
import com.example.domain.repo.auth.AuthRepo
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepoImpl(private val firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) : AuthRepo {
    val currentUser: FirebaseUser? = firebaseAuth.currentUser
    private val usersRef: CollectionReference = firebaseFirestore.collection(USERS_COLLECTION_REF)

    override suspend fun hasUser(): Flow<Resources<Boolean>> = flow {
        try {
             emit(Resources.Success(firebaseAuth.currentUser != null))
        }catch (e: Exception){
            emit(Resources.Error(e.message))
        }
    }

    override suspend fun getUserId(): Flow<Resources<String>> = flow {
        try {
            emit(Resources.Success(firebaseAuth.currentUser?.uid.orEmpty()))
        }catch (e: Exception){
            emit(Resources.Error(e.message))
        }
    }

    override suspend fun createUser(
        name: String,
        email: String,
        password: String
    ): Flow<Resources<User>> = flow {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                currentUser.updateProfile(profileUpdates).await()

                val user = User(
                    userId = currentUser.uid ?: "",
                    userName = currentUser.displayName ?: "",
                    userEmail = currentUser.email ?: "",
                    password = password
                )
                emit(Resources.Success(user))
            }else {
                emit(Resources.Error("Failed to retrieve user data."))
            }
//                }
        }catch (e: Exception){
            emit(Resources.Error(e.message))
        }
    }


    override suspend fun login(
        email: String,
        password: String
    ): Flow<Resources<User>> = flow {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null){
                val user = User(
                    userId = firebaseAuth.uid ?: "",
                    userEmail = currentUser.email ?: "",
                    userName = currentUser.displayName?:"",
                    password = password
                )
                emit(Resources.Success(user))
            }else {
                emit(Resources.Error("Failed to retrieve user data."))
            }

        }catch (e: Exception){
            emit(Resources.Error(e.message))
        }
    }

    override suspend fun getUserInfo(): Flow<Resources<User?>> = callbackFlow {
        trySend(Resources.Loading)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val user = User(
                userId = firebaseAuth.uid ?: "",
                userName = currentUser.displayName ?: "",
                userEmail = currentUser.email ?: ""
            )
            usersRef.document(firebaseAuth.uid ?: "").set(user).await()

            val listener = usersRef.document(firebaseAuth.uid ?: "")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Resources.Error(error.message))
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val userObj = snapshot.toObject(User::class.java)
                        userObj?.let {
                            trySend(Resources.Success(it))
                        }
                    } else {
                        trySend(Resources.Error("User not found"))
                    }
                }

            // Close flow when no longer used
            awaitClose { listener.remove() }
        }
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
//            Log.i("user naaaaame impl", currentUser.displayName?:"no name :(")
//            val userInfo = User(
//                userName = currentUser.displayName?:"",
//                userEmail = currentUser.email?:""
//            )
//            emit(Resources.Success(userInfo))
//        } else {
//            emit(Resources.Error(null))
//        }
    }

    override suspend fun updateUserName(name: String): Flow<Resources<User>> = flow {
        emit(Resources.Loading)
        if (currentUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            try {
                currentUser.updateProfile(profileUpdates).await()
                val user = User(userName = name)
                emit(Resources.Success(user))
            } catch (e: Exception) {
                emit(Resources.Error(e.message))
            }
        } else {
            emit(Resources.Success(User()))
        }
    }

    override suspend fun updatePassword(currentPassword: String, newPassword: String, confirmNewPassword: String): Flow<Resources<User>> = flow {
        emit(Resources.Loading)
        if (currentUser != null) {
            try {
                val email = currentUser.email ?: throw Exception("User email not found.")
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                currentUser.reauthenticate(credential).await()

                currentUser.updatePassword(newPassword).await()
                val user = User(password = newPassword)
                emit(Resources.Success(user))
            } catch (e: Exception) {
                emit(Resources.Error(e.message))
            }
        } else {
            emit(Resources.Success(User()))
        }
    }

    override suspend fun logout(): Flow<Resources<User>> = flow {
        try {
            firebaseAuth.signOut()
            val user = User()
            emit(Resources.Success(user))

        }catch (e: Exception){
            emit(Resources.Error(e.message))
        }
    }

    override suspend fun deleteUserAccount(): Flow<Resources<Unit>> = flow {
        emit(Resources.Loading)

        if (currentUser != null) {
            try {
                currentUser.delete().await()
                emit(Resources.Success(Unit))
            } catch (e: Exception) {
                emit(Resources.Error(e.message))
            }
        } else {
            emit(Resources.Error("No user is currently logged in")) // Emit error if no user
        }
    }

}