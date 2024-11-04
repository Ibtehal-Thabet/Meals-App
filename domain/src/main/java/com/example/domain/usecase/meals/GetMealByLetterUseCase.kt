package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealRepo

class GetMealByLetterUseCase(private val mealRepo: MealRepo) {

    suspend operator fun invoke(firstLetter: String) = mealRepo.getMealByLetterFromRemote(firstLetter)
}