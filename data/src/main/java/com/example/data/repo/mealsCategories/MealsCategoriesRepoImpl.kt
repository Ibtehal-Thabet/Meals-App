package com.example.data.repo.mealsCategories

import com.example.data.remote.meals.MealsApi
import com.example.domain.repo.meals.MealsCategoriesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealsCategoriesRepoImpl(private val mealsApi: MealsApi) : MealsCategoriesRepo {

    override suspend fun getMealCategoriesFromRemote() = withContext(Dispatchers.IO) {
        return@withContext mealsApi.getMealsCategoriesRequest()
    }
}