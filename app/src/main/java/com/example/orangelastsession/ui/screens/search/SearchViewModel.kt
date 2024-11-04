package com.example.orangelastsession.ui.screens.search

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.meals.FilterMeal
import com.example.domain.entity.meals.FilterMealsResponse
import com.example.domain.entity.meals.Meal
import com.example.domain.usecase.meals.ConvertFilterMealsToMealsUseCase
import com.example.domain.usecase.meals.GetMealByAreaUseCase
import com.example.domain.usecase.meals.GetMealByCategoryUseCase
import com.example.domain.usecase.search.GetSearchMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchMealsUseCase: GetSearchMealsUseCase,
): ViewModel(){

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchMealList = MutableStateFlow<List<Meal>>(emptyList())
    val searchMealList = _searchMealList.asStateFlow()

    var isSearching = mutableStateOf(false)

//    init {
//        searchMeals()
//    }

    fun searchMeals(){
        try {
            isSearching.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val data  = getSearchMealsUseCase(_searchText.value)
                _searchMealList.update { data.meals }
                isSearching.value = false
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

    fun onSearchTextChange(text: String){
        _searchText.value = text
    }
}