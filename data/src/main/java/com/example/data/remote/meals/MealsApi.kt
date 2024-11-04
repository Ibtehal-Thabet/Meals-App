package com.example.data.remote.meals

import com.example.domain.entity.meals.MealResponse
import com.example.domain.entity.meals.FilterMealsResponse
import com.example.domain.entity.mealsArea.AreaResponse
import com.example.domain.entity.mealsCategories.CategoriesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApi {

    @GET("categories.php")
    suspend fun getMealsCategoriesRequest() : CategoriesResponse

    @GET("list.php?a=list")
    suspend fun getAreasRequest() : AreaResponse

    @GET("random.php")
    suspend fun getRandomMealsRequest() : MealResponse

    @GET("search.php")
    suspend fun getMealsByLetterRequest(@Query("f") firstLetter: String) : MealResponse

    @GET("search.php")
    suspend fun searchMealsRequest(@Query("s") searchMeal: String) : MealResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") mealId: String) : MealResponse

    @GET("filter.php")
    suspend fun getMealsByCategoryRequest(@Query("c") category: String) : FilterMealsResponse

    @GET("filter.php")
    suspend fun getMealsByAreaRequest(@Query("a") area: String) : FilterMealsResponse

    @GET("filter.php")
    suspend fun getMealsByIngredientRequest(@Query("i") ingredient: String) : FilterMealsResponse

}