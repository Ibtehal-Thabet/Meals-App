package com.example.data.repo.local

import com.example.data.local.MealDao
import com.example.domain.entity.meals.Meal
import com.example.domain.repo.local.MealLocalRepo
import com.example.data.mappers.toEntity
import com.example.data.mappers.toMeal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MealLocalRepoImpl(private val mealDao: MealDao): MealLocalRepo {
    override suspend fun getMeals(): Flow<List<Meal>> = flow {
        val meals = mealDao.getAllMeals().map {
            it.toMeal()
        }
        emit(meals)
    }

    suspend fun insertMeals(meals: List<Meal>) {
        mealDao.insertMeals(meals.map { it.toEntity() })
    }
}