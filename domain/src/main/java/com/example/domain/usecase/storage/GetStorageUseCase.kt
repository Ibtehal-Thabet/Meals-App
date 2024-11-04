package com.example.domain.usecase.storage

import com.example.domain.entity.meals.Meal
import com.example.domain.entity.storage.FavoriteMeals
import com.example.domain.repo.storage.StorageRepo
import kotlinx.coroutines.flow.Flow


class GetStorageUseCase(private val storageRepo: StorageRepo) {
    suspend fun invokeAddToFav(userId: String,
                                meal: Meal) = storageRepo.addFavoriteMeal(userId, meal)

    suspend fun invokeRemoveFromFav(userId: String,
                                    mealId: String) = storageRepo.removeFavoriteMeal(userId, mealId)

    suspend fun getUserFavorites(userId: String) = storageRepo.getUserFavorites(userId)

    suspend fun getFavoriteMeal(mealFirestoreId: String) = storageRepo.getFavoriteMeal(mealFirestoreId)

    fun isMealFavorite(userId: String, mealId: String) = storageRepo.isMealFavorite(userId, mealId)
}