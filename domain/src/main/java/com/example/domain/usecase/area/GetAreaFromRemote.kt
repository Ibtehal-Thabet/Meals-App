package com.example.domain.usecase.area

import com.example.domain.repo.area.MealsAreasRepo

class GetAreaFromRemote (private val mealsAreasRepo: MealsAreasRepo) {
    suspend operator fun invoke() = mealsAreasRepo.getAreasFromRemote()

}
