package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealRepo

class GetMealByAreaUseCase(private val mealRepo: MealRepo) {
    suspend operator fun invoke(area: String) = mealRepo.getMealByAreaFromRemote(area)
}