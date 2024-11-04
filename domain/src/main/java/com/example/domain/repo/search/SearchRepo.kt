package com.example.domain.repo.search

import com.example.domain.entity.meals.MealResponse

interface SearchRepo {

    suspend fun searchMealsRequest(searchMeal: String) : MealResponse
}