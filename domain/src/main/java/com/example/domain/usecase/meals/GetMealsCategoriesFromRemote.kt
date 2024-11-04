package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealsCategoriesRepo

class GetMealsCategoriesFromRemote (private val mealsCategoriesRepo: MealsCategoriesRepo) {
    suspend operator fun invoke() = mealsCategoriesRepo.getMealCategoriesFromRemote()

}
