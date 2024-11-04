package com.example.domain.usecase

import com.example.domain.usecase.area.GetAreaFromRemote
import com.example.domain.usecase.meals.ConvertFilterMealsToMealsUseCase
import com.example.domain.usecase.meals.GetMealByAreaUseCase
import com.example.domain.usecase.meals.GetMealByCategoryUseCase
import com.example.domain.usecase.meals.GetMealByIdUseCase
import com.example.domain.usecase.meals.GetMealByIngredientUseCase
import com.example.domain.usecase.meals.GetMealByLetterUseCase
import com.example.domain.usecase.meals.GetMealsCategoriesFromRemote
import com.example.domain.usecase.meals.GetRandomMealUseCase

data class MealsUseCases(
    val getRandomMealUseCase: GetRandomMealUseCase,
    val getMealsCategoriesFromRemote: GetMealsCategoriesFromRemote,
    val getAreaFromRemote: GetAreaFromRemote,
    val getMealsByCategoryUseCase: GetMealByCategoryUseCase,
    val getMealsByAreaFromRemote: GetMealByAreaUseCase,
    val getMealByIngredientUseCase: GetMealByIngredientUseCase,
    val getMealsByLetterFromRemote: GetMealByLetterUseCase,
    val getMealByIdUseCase: GetMealByIdUseCase,
    val convertFilterMealsToMealsUseCase: ConvertFilterMealsToMealsUseCase,
)
