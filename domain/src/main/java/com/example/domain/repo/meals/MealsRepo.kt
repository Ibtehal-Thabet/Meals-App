package com.example.domain.repo.meals

import com.example.domain.entity.meals.Category
import com.example.domain.entity.meals.MealsModelResponse

interface MealsRepo {
    suspend fun getMealCategoriesFromRemote() : MealsModelResponse
}