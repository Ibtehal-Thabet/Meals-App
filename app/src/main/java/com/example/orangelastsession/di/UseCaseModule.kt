package com.example.orangelastsession.di


import com.example.domain.repo.area.MealsAreasRepo
import com.example.domain.repo.auth.AuthRepo
import com.example.domain.repo.dataStore.DataStoreRepo
import com.example.domain.repo.local.MealLocalRepo
import com.example.domain.repo.meals.MealRepo
import com.example.domain.repo.meals.MealsCategoriesRepo
import com.example.domain.repo.search.SearchRepo
import com.example.domain.repo.storage.StorageRepo
import com.example.domain.usecase.MealsUseCases
import com.example.domain.usecase.area.GetAreaFromRemote
import com.example.domain.usecase.auth.GetUserUseCase
import com.example.domain.usecase.dataStore.GetDataStoreUseCase
import com.example.domain.usecase.local.GetMealsLocalUseCase
import com.example.domain.usecase.meals.ConvertFilterMealsToMealsUseCase
import com.example.domain.usecase.meals.GetMealByAreaUseCase
import com.example.domain.usecase.meals.GetMealByCategoryUseCase
import com.example.domain.usecase.meals.GetMealByIdUseCase
import com.example.domain.usecase.meals.GetMealByIngredientUseCase
import com.example.domain.usecase.meals.GetMealByLetterUseCase
import com.example.domain.usecase.meals.GetMealsCategoriesFromRemote
import com.example.domain.usecase.meals.GetRandomMealUseCase
import com.example.domain.usecase.search.GetSearchMealsUseCase
import com.example.domain.usecase.storage.GetStorageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetMealsUseCase(mealsRepo: MealRepo, mealsCategoriesRepo: MealsCategoriesRepo, mealsAreasRepo: MealsAreasRepo): MealsUseCases {
        return MealsUseCases(GetRandomMealUseCase(mealsRepo),
            GetMealsCategoriesFromRemote(mealsCategoriesRepo),
            GetAreaFromRemote(mealsAreasRepo),
            GetMealByCategoryUseCase(mealsRepo),
            GetMealByAreaUseCase(mealsRepo),
            GetMealByIngredientUseCase(mealsRepo),
            GetMealByLetterUseCase(mealsRepo),
            GetMealByIdUseCase(mealsRepo),
            ConvertFilterMealsToMealsUseCase(mealsRepo)
            )
    }

    @Provides
    fun provideGetSearchMealUseCase(searchRepo: SearchRepo): GetSearchMealsUseCase {
        return GetSearchMealsUseCase(searchRepo)
    }

    @Provides
    fun provideGetDataStoreUseCase(dataStoreRepo: DataStoreRepo): GetDataStoreUseCase {
        return GetDataStoreUseCase(dataStoreRepo)
    }

//    @Provides
//    fun provideGetMealsLocalUseCase(mealLocalRepo: MealLocalRepo) = GetMealsLocalUseCase(mealLocalRepo)

    @Provides
    fun provideGetUserUseCase(authRepo: AuthRepo): GetUserUseCase {
        return GetUserUseCase(authRepo)
    }

    @Provides
    fun provideGetStorageUseCase(storageRepo: StorageRepo): GetStorageUseCase {
        return GetStorageUseCase(storageRepo)
    }
}