package com.example.data.repo.storage

import android.util.Log
import com.example.domain.common.Resources
import com.example.domain.entity.meals.Meal
import com.example.domain.entity.storage.FavoriteMeals
import com.example.domain.repo.storage.StorageRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

const val FAVORITES_MEALS_COLLECTION_REF = "favorites"
const val USERS_COLLECTION_REF = "users"

class StorageRepoImpl(firebaseFirestore: FirebaseFirestore): StorageRepo{
    private var isAddedSuccessful = false
    private var isRemovedSuccessful = false
    private var isInFav = false

    private val usersRef: CollectionReference = firebaseFirestore.collection(USERS_COLLECTION_REF)
    private val favoritesRef: CollectionReference = firebaseFirestore.collection(FAVORITES_MEALS_COLLECTION_REF)
    override suspend fun getUserFavorites(userId: String): Flow<Resources<List<Meal>>> = callbackFlow {
        trySend(Resources.Loading)
        try {
            val favMeals = mutableListOf<Meal>()
            val querySnapshot = usersRef
                .document(userId)
                .collection("favorites")
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val meal = document.toObject(Meal::class.java)
                meal?.let { favMeals.add(it) }
            }
            trySend(Resources.Success(data = favMeals))
        } catch (e: Exception) {
            trySend(Resources.Error(message = e.message))
        }
//        var snapshotStateListener: ListenerRegistration? = null
//
//        try {
//            snapshotStateListener = favoritesRef
//                .whereEqualTo("userId", userId)
//                .addSnapshotListener{snapshot,  e ->
//                    val response = if(snapshot != null){
//                        val favoriteMeals = snapshot.toObjects(FavoriteMeals::class.java)
//                        Resources.Success(data = favoriteMeals)
//                    }else{
//                        Resources.Error(message = e?.message)
//                    }
//                    trySend(response).isSuccess
//
//                }
//
//
//        }catch (e: Exception){
//            trySend(Resources.Error(e.message))
//            e.printStackTrace()
//        }
//
        awaitClose{
//            querySnapshot?.remove()
        }
    }

    override suspend fun getFavoriteMeal(
        mealFirestoreId: String,
    ): Flow<Resources<Boolean>> = flow {
        isInFav = false
        try {
            favoritesRef
                .document(mealFirestoreId)
                .get()
                .addOnSuccessListener {
                    isInFav = true
                }.addOnFailureListener {
                    isInFav = false
                }.await()
            if (isInFav){
                emit(Resources.Success(isInFav))
            }else{
                emit(Resources.Error("Meal is not in favorite"))
            }

        }catch (e: Exception){
            emit(Resources.Error(e.message?:"Unknown Error"))
        }
    }

    override suspend fun addFavoriteMeal(
        userId: String,
        meal: Meal
    ): Flow<Resources<Boolean>> = flow {
        isAddedSuccessful = false
        try{
            usersRef.document(userId)
                .collection("favorites")
                .document(meal.idMeal?:"")
                .set(meal)
                .addOnCompleteListener { result ->
                    isAddedSuccessful = true
                }.await()
            if (isAddedSuccessful){
                emit(Resources.Success(isAddedSuccessful))
            }else{
                emit(Resources.Error("Meal add Unsuccessfully"))
            }

        }catch (e: Exception){
            emit(Resources.Error(e.message?:"Unknown Error"))
        }
    }

    override suspend fun removeFavoriteMeal(
        userId: String,
        mealId: String): Flow<Resources<Boolean>> = flow {
        isRemovedSuccessful = false
        try{
            usersRef.document(userId)
                .collection("favorites")
                .document(mealId)
                .delete()
                .addOnCompleteListener { result ->
                isRemovedSuccessful = true
            }.await()
            if (isRemovedSuccessful){
                emit(Resources.Success(isRemovedSuccessful))
            }else{
                emit(Resources.Error("Meal add Unsuccessfully"))
            }

        }catch (e: Exception){
            emit(Resources.Error(e.message?:"Unknown Error"))
        }
    }

    override fun isMealFavorite(userId: String, mealId: String): Flow<Boolean> = callbackFlow {
        val docRef = usersRef.document(userId)
            .collection("favorites")
            .document(mealId)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.exists() ?: false).isSuccess  // Check if the meal exists in Firestore
        }

        awaitClose { listener.remove() }
    }
}
