package com.example.domain.repo.meals

import com.example.domain.entity.mealsCategories.CategoriesResponse

interface MealsCategoriesRepo {
    suspend fun getMealCategoriesFromRemote() : CategoriesResponse
}