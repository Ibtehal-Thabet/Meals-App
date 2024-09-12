package com.example.orangelastsession.di


import com.example.data.remote.meals.MealsApi
import com.example.data.repo.meals.MealsRepoImpl
import com.example.domain.repo.meals.MealsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun providedMealRepo(
        mealsApiService: MealsApi,
    ): MealsRepo {
        return MealsRepoImpl(mealsApiService)
    }
}