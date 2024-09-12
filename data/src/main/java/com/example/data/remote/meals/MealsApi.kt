package com.example.data.remote.meals

import com.example.domain.entity.meals.MealsModelResponse
import retrofit2.http.GET

interface MealsApi {

    @GET("categories.php")
    suspend fun getMealsRequest() : MealsModelResponse

}