package com.example.data.repo.meals

import com.example.data.remote.meals.MealsApi
import com.example.domain.entity.meals.FilterMealsResponse
import com.example.domain.entity.mealsArea.AreaResponse
import com.example.domain.repo.meals.MealRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealsRepoImpl(private val mealsApi: MealsApi) : MealRepo {
    override suspend fun getMealByLetterFromRemote(firstLetter: String) = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getMealsByLetterRequest(firstLetter)
    }

    override suspend fun getMealByCategoryFromRemote(category: String): FilterMealsResponse = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getMealsByCategoryRequest(category)
    }

    override suspend fun getMealByAreaFromRemote(area: String): FilterMealsResponse = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getMealsByAreaRequest(area)
    }

    override suspend fun getMealByIngredientFromRemote(ingredient: String): FilterMealsResponse = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getMealsByIngredientRequest(ingredient)
    }

    override suspend fun getRandomMealFromRemote() = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getRandomMealsRequest()
    }

    override suspend fun getMealByIdFromRemote(mealId: String) = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getMealById(mealId)
    }
 }