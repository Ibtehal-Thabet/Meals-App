package com.example.domain.usecase.meals

import com.example.domain.repo.meals.MealsRepo

class GetMealsCategoriesFromRemote (private val mealsRepo: MealsRepo) {
    suspend operator fun invoke() = mealsRepo.getMealCategoriesFromRemote()

}
