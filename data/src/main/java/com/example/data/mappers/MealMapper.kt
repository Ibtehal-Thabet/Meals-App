package com.example.data.mappers

import com.example.data.local.MealEntity
import com.example.domain.entity.meals.Meal

fun MealEntity.toMeal(): Meal = Meal(idMeal = idMeal, strMeal = strMeal, strMealThumb = strMealThumb, strCategory = strCategory, strArea = strArea)
fun Meal.toEntity(): MealEntity = MealEntity(idMeal = idMeal?:"", strMeal = strMeal, strMealThumb = strMealThumb, strCategory = strCategory, strArea = strArea)