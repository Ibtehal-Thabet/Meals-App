package com.example.orangelastsession.ui.screens.meals

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MealEntity
import com.example.domain.common.Resources
import com.example.domain.entity.meals.FilterMeal
import com.example.domain.entity.meals.Meal
import com.example.domain.entity.meals.MealResponse
import com.example.domain.entity.mealsArea.AreaMeal
import com.example.domain.entity.mealsCategories.Category
import com.example.domain.usecase.MealsUseCases
import com.example.domain.usecase.auth.GetUserUseCase
import com.example.domain.usecase.local.GetMealsLocalUseCase
import com.example.domain.usecase.storage.GetStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(
    private val mealsUseCases: MealsUseCases,
    private val getStorageUseCase: GetStorageUseCase
): ViewModel() {

//    private val _localMeals = MutableStateFlow<List<Meal>>(emptyList())
//    val localMeals = _localMeals.asStateFlow()


    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _areas = MutableStateFlow<List<AreaMeal>>(emptyList())
    val areas = _areas.asStateFlow()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals = _meals.asStateFlow()

    private val _mealById = MutableStateFlow<Meal>(Meal())
    val mealById: StateFlow<Meal> = _mealById

    private  val _randomMeal = MutableStateFlow<MealResponse>(MealResponse(emptyList()))
    val randomMeal = _randomMeal.asStateFlow()

    private val _favoriteMeals = MutableStateFlow<Map<String?, Boolean>>(emptyMap())
    val favoriteMeals: StateFlow<Map<String?, Boolean>> = _favoriteMeals.asStateFlow()

    private val _addMeal = MutableStateFlow<Boolean>(false)
    val addMeal = _addMeal.asStateFlow()

    private val _removeMeal = MutableStateFlow<Boolean>(false)
    val removeMeal = _removeMeal.asStateFlow()

    var isLoadingCategories = mutableStateOf(true)
    var isLoading = mutableStateOf(true)
    var isLoadingPager = mutableStateOf(true)

    init {
        getCategories()
        getMealsByCategory(category = "Beef")

        getMealsByLetter("a")

        repeat(5) {
            getRandomMeal()
        }
    }

//    private fun getLocalMeals(){
//        try {
//            viewModelScope.launch {
//                getMealsLocalUseCase().collect {
//                    _localMeals.value = it
//                }
//            }
//        }catch (e: Exception) {
//            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
//                        Build.VERSION_CODES.S
//                    ) >= 7
//                ) {
//                    e is HttpException
//                } else {
//                    e is IllegalArgumentException
//                }
//            ) {
//                Log.i("Exp", e.localizedMessage.toString())
//            } else {
//                Log.i("Exp", e.message.toString())
//            }
//        }
//    }

    private fun getCategories(){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val data = mealsUseCases.getMealsCategoriesFromRemote()
                _categories.update { data.categories }
                isLoadingCategories.value = false
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

    fun getAreas(){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val data = mealsUseCases.getAreaFromRemote()
                _areas.update { data.meals }
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

    private fun getMealsByFilterMeals(filterMeals: List<FilterMeal>) {
        try {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val meals = mealsUseCases.convertFilterMealsToMealsUseCase(filterMeals)
                _meals.update { meals }
                isLoading.value = false
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

    fun getMealsByCategory(category: String){
        try {
//            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val data  = mealsUseCases.getMealsByCategoryUseCase(category)
                getMealsByFilterMeals(data.meals)
//                isLoading.value = false
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

    fun getMealsByArea(area: String){
        try {
//            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val data  = mealsUseCases.getMealsByAreaFromRemote(area)
                getMealsByFilterMeals(data.meals)
//                isLoading.value = false
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

    fun getMealsByLetter(firstLetter: String){
        try {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val data  = mealsUseCases.getMealsByLetterFromRemote(firstLetter)
                _meals.update { data.meals }
                isLoading.value = false
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

    private fun getRandomMeal(){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val data  = mealsUseCases.getRandomMealUseCase.invoke()

                _randomMeal.update { currentMealResponse ->
                    val updatedMeals = currentMealResponse.meals.toMutableList().apply {
                        addAll(data.meals) // Add the new meals to the existing ones
                    }
                    // Return the new MealResponse with the updated meals
                    currentMealResponse.copy(meals = updatedMeals)
                }
                isLoadingPager.value = false
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

    private fun getMealById(mealId: String){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val data  = mealsUseCases.getMealByIdUseCase.invoke(mealId = mealId)
                _mealById.update { data.meals[0] }

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
//            val favoriteMap = mutableMapOf<String, Boolean>()
//            mealItems.forEach { mealItem ->
//                getStorageUseCase.isMealFavorite(userId, mealItem.idMeal ?: "")
//                    .collect { isFav ->
//                        _favoriteMeals.update { currentMap ->
//                            currentMap.toMutableMap().apply {
//                                put(mealItem.idMeal ?: "", isFav)
//                            }
//                        }
////                        favoriteMap[mealItem.idMeal ?: ""] = isFav
////                        _favoriteMeals.value = favoriteMap.toMap()
//                        Log.i("get isFav", _favoriteMeals.value.toString())
//                    }
//            }
        }
    }
}