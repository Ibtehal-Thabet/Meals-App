package com.example.data.repo.mealsAreas

import android.util.Log
import com.example.data.remote.meals.MealsApi
import com.example.domain.repo.area.MealsAreasRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealsAreasRepoImpl(private val mealsApi: MealsApi) : MealsAreasRepo {

    override suspend fun getAreasFromRemote() = withContext(Dispatchers.IO) {
//        Log.i("area", mealsApi.getAreasRequest().areaList.size.toString())
        return@withContext mealsApi.getAreasRequest()
    }
}