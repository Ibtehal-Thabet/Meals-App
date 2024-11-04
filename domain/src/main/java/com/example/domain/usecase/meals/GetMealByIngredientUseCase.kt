package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealRepo

class GetMealByIngredientUseCase(private val mealRepo: MealRepo) {
    suspend operator fun invoke(ingredient: String) = mealRepo.getMealByIngredientFromRemote(ingredient)
}