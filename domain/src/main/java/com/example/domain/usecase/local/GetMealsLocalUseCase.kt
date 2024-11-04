package com.example.domain.usecase.local

import com.example.domain.entity.meals.Meal
import com.example.domain.repo.local.MealLocalRepo
import kotlinx.coroutines.flow.Flow

class GetMealsLocalUseCase(private val mealLocalRepo: MealLocalRepo) {
    suspend operator fun invoke(): Flow<List<Meal>> = mealLocalRepo.getMeals()

}