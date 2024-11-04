package com.example.orangelastsession.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.local.AppDatabase
import com.example.data.local.MealDao
import com.example.data.repo.local.MealLocalRepoImpl
import com.example.domain.repo.local.MealLocalRepo
import com.example.domain.repo.meals.MealRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATASTORE_NAME = "theme"

// Define the DataStore extension at the file level
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): androidx.datastore.core.DataStore<Preferences> {
        return context.dataStore
    }

//    @Singleton
//    @Provides
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return AppDatabase.getDatabase(context)
//    }
//
//    @Singleton
//    @Provides
//    fun provideMealDao(database: AppDatabase): MealDao = database.mealDao()
//
//    @Singleton
//    @Provides
//    fun provideMealRepository(mealDao: MealDao): MealLocalRepo = MealLocalRepoImpl(mealDao)

}
