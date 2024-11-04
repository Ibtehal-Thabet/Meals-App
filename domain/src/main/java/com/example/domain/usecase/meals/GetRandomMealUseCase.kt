package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealRepo

class GetRandomMealUseCase(private val mealRepo: MealRepo) {

    suspend operator fun invoke() = mealRepo.getRandomMealFromRemote()
}