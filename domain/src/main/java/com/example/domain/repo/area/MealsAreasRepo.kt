package com.example.domain.repo.area

import com.example.domain.entity.mealsArea.AreaResponse

interface MealsAreasRepo {
    suspend fun getAreasFromRemote() : AreaResponse
}