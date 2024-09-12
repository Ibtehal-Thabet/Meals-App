package com.example.orangelastsession.ui.screens.meals

import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.meals.MealsModelResponse
import com.example.domain.usecase.meals.GetMealsCategoriesFromRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor(private val getMealsCategoriesFromRemote: GetMealsCategoriesFromRemote): ViewModel() {

    private  val _categories = MutableStateFlow<MealsModelResponse>(MealsModelResponse(emptyList()))
    val category = _categories.asStateFlow()

    init {
        getMeals()
    }

    private fun getMeals(){
        try {
            viewModelScope.launch {
                val data  = getMealsCategoriesFromRemote()
                _categories.update { data }
            }
        }catch (e: Exception){
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                        Build.VERSION_CODES.S) >= 7) {
                    e is HttpException
                } else {
                    TODO("SdkExtensions.getExtensionVersion(S) < 7")
                }
            ){
                Log.i("Exp", e.localizedMessage.toString())
            }else{
                Log.i("Exp", e.message.toString())
            }

        }

    }
}