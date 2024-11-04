package com.example.domain.usecase.meals

import android.util.Log
import com.example.domain.entity.meals.FilterMeal
import com.example.domain.entity.meals.Meal
import com.example.domain.repo.meals.MealRepo

class ConvertFilterMealsToMealsUseCase (private val mealsRepo: MealRepo) {

    suspend operator fun invoke(categoryMeals: List<FilterMeal>): List<Meal> {
        val meals = mutableListOf<Meal>()

        for (categoryMeal in categoryMeals) {
            try {
                val meal = mealsRepo.getMealByIdFromRemote(categoryMeal.idMeal!!)
                meals.add(meal.meals[0])
            } catch (e: Exception) {
                // Handle exceptions, such as API errors
                Log.i("Exp", e.localizedMessage?:"Error")
            }
        }
        return meals
    }
}