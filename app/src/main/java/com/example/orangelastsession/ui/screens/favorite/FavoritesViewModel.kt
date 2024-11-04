package com.example.orangelastsession.ui.screens.favorite


import android.net.http.HttpException
import android.os.Build
import android.os.ext.SdkExtensions
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.Resources
import com.example.domain.entity.meals.Meal
import com.example.domain.usecase.auth.GetUserUseCase
import com.example.domain.usecase.storage.GetStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getStorageUseCase: GetStorageUseCase
): ViewModel() {

    private val _favoriteMeals = MutableStateFlow<List<Meal>>(emptyList())
    val favoriteMeals = _favoriteMeals.asStateFlow()

    private val _removeMeal = MutableStateFlow(false)
    val removeMeal = _removeMeal.asStateFlow()

    fun getAllFavoriteMeals(userId: String){
        viewModelScope.launch {
            getStorageUseCase.getUserFavorites(userId).collect{
//                Resources.Loading
                when(it) {
                    is Resources.Success -> {
                        _favoriteMeals.value = it.data?: listOf()
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("Meal in fav", "Error")
                    }

                    else -> {}
                }
            }
        }
    }

    fun removeFromFav(userId: String, mealId: String){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                getStorageUseCase.invokeRemoveFromFav(userId, mealId).collect{
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

}
