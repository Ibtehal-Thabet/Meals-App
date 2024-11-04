package com.example.domain.repo.local

import com.example.domain.entity.meals.Meal
import kotlinx.coroutines.flow.Flow

interface MealLocalRepo {

    suspend fun getMeals(): Flow<List<Meal>>
}