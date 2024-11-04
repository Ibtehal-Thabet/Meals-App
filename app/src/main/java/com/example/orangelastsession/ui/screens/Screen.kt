package com.example.orangelastsession.ui.screens


sealed class Screen(val route: String) {
    object SplashScreen : Screen("SplashScreen")
    object OnboardingScreen : Screen("OnboardingScreen")
    object LoginScreen : Screen("LoginScreen")
    object SignUpScreen : Screen("SignUpScreen")
    object MainScreen : Screen("MainScreen")
    object MealsScreen : Screen("MealsScreen")
    object SearchScreen : Screen("SearchScreen")
    object FavoriteScreen : Screen("FavoriteScreen")
    object ProfileScreen : Screen("ProfileScreen")
    object UpdateProfileInfoScreen: Screen("UpdateProfileInfoScreen")
    object MealDetailsScreen : Screen("MealDetailsScreen")
}