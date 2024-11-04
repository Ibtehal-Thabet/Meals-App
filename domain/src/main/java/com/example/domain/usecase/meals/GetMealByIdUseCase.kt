package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealRepo

class GetMealByIdUseCase(private val mealRepo: MealRepo) {

    suspend operator fun invoke(mealId: String) = mealRepo.getMealByIdFromRemote(mealId)
}