package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealRepo

class GetMealByCategoryUseCase(private val mealRepo: MealRepo) {
    suspend operator fun invoke(category: String) = mealRepo.getMealByCategoryFromRemote(category)
}