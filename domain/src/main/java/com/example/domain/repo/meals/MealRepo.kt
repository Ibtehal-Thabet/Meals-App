package com.example.domain.repo.meals

import com.example.domain.entity.meals.MealResponse
import com.example.domain.entity.meals.FilterMealsResponse
import com.example.domain.entity.mealsArea.AreaResponse

interface MealRepo {
    suspend fun getMealByLetterFromRemote(firstLetter: String) : MealResponse

    suspend fun getMealByCategoryFromRemote(category: String) : FilterMealsResponse

    suspend fun getMealByAreaFromRemote(area: String) : FilterMealsResponse

    suspend fun getMealByIngredientFromRemote(ingredient: String) : FilterMealsResponse

    suspend fun getRandomMealFromRemote() : MealResponse

    suspend fun getMealByIdFromRemote(mealId: String) : MealResponse
}