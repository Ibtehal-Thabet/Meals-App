package com.example.orangelastsession.ui.screens.mealsDeatails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.entity.meals.Category
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel() {

    private val argsCategory: String? = savedStateHandle["category"]
    lateinit var category: Category

    init {
        argsCategory?.let {
            category = Gson().fromJson(it, Category::class.java)
            Log.i("cattttt", category.strCategory?:"Nooooo")
        }
    }
}