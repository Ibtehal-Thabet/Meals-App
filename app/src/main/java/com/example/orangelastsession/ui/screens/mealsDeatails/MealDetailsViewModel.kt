package com.example.orangelastsession.ui.screens.mealsDeatails

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.Resources
import com.example.domain.entity.meals.FilterMeal
import com.example.domain.entity.meals.Meal
import com.example.domain.entity.meals.FilterMealsResponse
import com.example.domain.entity.storage.FavoriteMeals
import com.example.domain.usecase.MealsUseCases
import com.example.domain.usecase.meals.ConvertFilterMealsToMealsUseCase
import com.example.domain.usecase.meals.GetMealByAreaUseCase
import com.example.domain.usecase.storage.GetStorageUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

@HiltViewModel
class MealDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mealsUseCases: MealsUseCases,
    private val getStorageUseCase: GetStorageUseCase
): ViewModel() {

    private val argsMeal: String? = savedStateHandle["meal"]
    lateinit var meal: Meal

    private val _ingredients = MutableStateFlow<List<String>>(emptyList())
    val ingredients: StateFlow<List<String>> = _ingredients

    private  val _mealsByIngredient = MutableStateFlow<FilterMealsResponse>(FilterMealsResponse(emptyList()))
    val mealsByIngredient = _mealsByIngredient.asStateFlow()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals = _meals.asStateFlow()

    private val _favoriteMeals = MutableStateFlow<Map<String?, Boolean>>(emptyMap())
    val favoriteMeals: StateFlow<Map<String?, Boolean>> = _favoriteMeals.asStateFlow()

    private val _addMeal = MutableStateFlow<Boolean>(false)
    val addMeal = _addMeal.asStateFlow()

    private val _removeMeal = MutableStateFlow<Boolean>(false)
    val removeMeal = _removeMeal.asStateFlow()

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite = _isFavorite.asStateFlow()

    var isLoading = mutableStateOf(true)

    init {

        argsMeal?.let {
            meal = Gson().fromJson(it, Meal::class.java)

            viewModelScope.launch {
                val ingredientsList = mutableListOf<String>()
                for (i in 1..20) {
                    val ingredientProperty = "strIngredient$i"
                    val measureProperty = "strMeasure$i"

                    val ingredient = Meal::class.memberProperties
                        .find {
                            it.name == ingredientProperty
                        }
                        ?.get(meal) as? String

                    val measure = Meal::class.memberProperties
                        .find {
                            it.name == measureProperty
                        }
                        ?.get(meal) as? String

                    if (!ingredient.isNullOrEmpty()) {
                        ingredientsList.add("$measure $ingredient")
                    } else {
                        break
                    }
                }
                _ingredients.value = ingredientsList
            }

            getMealsByIngredient(meal.strIngredient1?:"Beef")
        }
    }

    private fun getMealsByIngredient(ingredient: String){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val data  = mealsUseCases.getMealByIngredientUseCase(ingredient)
                getMealsByIngredientMeals(data.meals)
                _mealsByIngredient.update { data }
            }
        }catch (e: Exception){
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S) >= 7) {
                    e is HttpException
                } else {
                    true
                }
            ){
                Log.i("Exp", e.localizedMessage?:"Exception")
            }else{
                Log.i("Exp", e.message.toString())
            }
        }
    }

    private fun getMealsByIngredientMeals(ingredientMeals: List<FilterMeal>) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val meals = mealsUseCases.convertFilterMealsToMealsUseCase(ingredientMeals)
                _meals.update { meals }
                isLoading.value = false
            }
        }catch (e: Exception){
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S) >= 7) {
                    e is HttpException
                } else {
                    true
                }
            ){
                Log.i("Exp", e.localizedMessage?:"Exception")
            }else{
                Log.i("Exp", e.message.toString())
            }

        }
    }

    fun addToFav(userId: String, meal: Meal){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                getStorageUseCase.invokeAddToFav(
                    userId,
                    meal
                ).collect{
                    when(it) {
                        is Resources.Success -> {
                            _addMeal.value = it.data ?: true
                        }

                        is Resources.Loading -> {

                        }

                        is Resources.Error -> {
                            Log.i("Add to fav", it.message.toString())
                        }

                        else -> {}
                    }

                    Log.i("add", it.toString())
                }
            }
        }catch (e: Exception){
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S) >= 7) {
                    e is HttpException
                } else {
                    e is IllegalArgumentException
                }
            ){
                Log.i("Exp", e.localizedMessage.toString())
            }else{
                Log.i("Exp", e.message.toString())
            }
        }
    }

    fun removeFomFav(userId: String, meal: Meal){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                getStorageUseCase.invokeRemoveFromFav(userId, meal.idMeal?:"").collect{
                    when(it) {
                        is Resources.Success -> {
                            _removeMeal.value = it.data ?: true
                        }

                        is Resources.Loading -> {

                        }

                        is Resources.Error -> {
                            Log.i("Remove from fav", it.message.toString())
                        }

                        else -> {}
                    }
                }

            }
        }catch (e: Exception){
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S) >= 7) {
                    e is HttpException
                } else {
                    e is IllegalArgumentException
                }
            ){
                Log.i("Exp", e.localizedMessage.toString())
            }else{
                Log.i("Exp", e.message.toString())
            }
        }
    }

    fun loadFavoriteMeals(userId: String, mealItems: List<Meal>) {
        viewModelScope.launch {
            val deferredResults = mealItems.map { mealItem ->
                async {
                    val isFavorite = getStorageUseCase.isMealFavorite(userId, mealItem.idMeal ?: "").first()
                    mealItem.idMeal to isFavorite
                }
            }
            // Await all results and update the map
            val result = deferredResults.awaitAll().toMap()
            _favoriteMeals.value = result
        }
    }

    fun isFavoriteMeal(userId: String, mealItem: Meal) {
        viewModelScope.launch {
            val isFavorite = getStorageUseCase.isMealFavorite(userId, mealItem.idMeal ?: "").first()
            _isFavorite.value = isFavorite
        }
    }
}