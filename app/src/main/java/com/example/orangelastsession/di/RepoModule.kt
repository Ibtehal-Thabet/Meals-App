package com.example.orangelastsession.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.data.remote.meals.MealsApi
import com.example.data.repo.auth.AuthRepoImpl
import com.example.data.repo.dataStore.DataStoreRepoImpl
import com.example.data.repo.meals.MealsRepoImpl
import com.example.data.repo.mealsAreas.MealsAreasRepoImpl
import com.example.data.repo.mealsCategories.MealsCategoriesRepoImpl
import com.example.data.repo.search.SearchRepoImpl
import com.example.data.repo.storage.StorageRepoImpl
import com.example.domain.repo.area.MealsAreasRepo
import com.example.domain.repo.auth.AuthRepo
import com.example.domain.repo.dataStore.DataStoreRepo
import com.example.domain.repo.meals.MealRepo
import com.example.domain.repo.meals.MealsCategoriesRepo
import com.example.domain.repo.search.SearchRepo
import com.example.domain.repo.storage.StorageRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    ): MealsCategoriesRepo {
        return MealsCategoriesRepoImpl(mealsApiService)
    }

    @Provides
    fun providedAreaRepo(
        mealsApiService: MealsApi,
    ): MealsAreasRepo {
        return MealsAreasRepoImpl(mealsApiService)
    }

    @Provides
    fun providedMealByCatRepo(
        mealsApiService: MealsApi,
    ): MealRepo {
        return MealsRepoImpl(mealsApiService)
    }

    @Provides
    fun providedSearchMeal(
        mealsApiService: MealsApi,
    ): SearchRepo {
        return SearchRepoImpl(mealsApiService)
    }

    @Provides
    fun providedDataStore(
        dataStore: DataStore<Preferences>
    ): DataStoreRepo {
        return DataStoreRepoImpl(dataStore)
    }

    @Provides
    fun providedLoginUser(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): AuthRepo {
        return AuthRepoImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    fun providedFirestore(
        firebaseFirestore: FirebaseFirestore,
    ): StorageRepo {
        return StorageRepoImpl(firebaseFirestore)
    }

}