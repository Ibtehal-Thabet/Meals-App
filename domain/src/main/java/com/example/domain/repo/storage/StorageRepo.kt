package com.example.domain.repo.storage

import com.example.domain.common.Resources
import com.example.domain.entity.meals.Meal
import com.example.domain.entity.storage.FavoriteMeals
import kotlinx.coroutines.flow.Flow

interface StorageRepo {
    suspend fun getUserFavorites(userId: String): Flow<Resources<List<Meal>>>

    suspend fun getFavoriteMeal(mealFirestoreId: String): Flow<Resources<Boolean>>

    suspend fun addFavoriteMeal(userId: String,
                                meal: Meal): Flow<Resources<Boolean>>

    suspend fun removeFavoriteMeal(userId: String,
                                   mealId: String): Flow<Resources<Boolean>>

    fun isMealFavorite(userId: String, mealId: String): Flow<Boolean>

}