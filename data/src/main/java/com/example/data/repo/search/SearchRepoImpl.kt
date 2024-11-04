package com.example.data.repo.search

import com.example.data.remote.meals.MealsApi
import com.example.domain.repo.meals.MealsCategoriesRepo
import com.example.domain.repo.search.SearchRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepoImpl(private val mealsApi: MealsApi) : SearchRepo {

    override suspend fun searchMealsRequest(searchMeal: String) = withContext(Dispatchers.IO) {
        return@withContext mealsApi.searchMealsRequest(searchMeal)
    }
}