package com.example.orangelastsession

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.orangelastsession.ui.screens.meals.MealsScreen
import com.example.orangelastsession.ui.screens.mealsDeatails.MealDetailsScreen
import com.example.orangelastsession.ui.theme.OrangeLastSessionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrangeLastSessionTheme {
                // A surface container using the 'background' color from the theme
                Box(
                    modifier = Modifier.fillMaxSize()) {
                    MealsNavHost()
                }
            }
        }
    }
}

@Composable
fun MealsNavHost() {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = "MealsScreen"){
        composable("MealsScreen"){ MealsScreen(navHostController) }
        composable("MealDetailsScreen?category={category}", arguments = listOf(navArgument("category"){NavType.StringType})){
            MealDetailsScreen(navHostController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    OrangeLastSessionTheme {
//        MealsNavHost()
    }
}