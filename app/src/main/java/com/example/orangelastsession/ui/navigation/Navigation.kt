package com.example.orangelastsession.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.domain.entity.auth.User
import com.example.orangelastsession.AnimatedNavigationBar
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.NetworkViewModel
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.screens.auth.AuthViewModel
import com.example.orangelastsession.ui.screens.auth.LoginScreen
import com.example.orangelastsession.ui.screens.auth.SignUpScreen
import com.example.orangelastsession.ui.screens.favorite.FavoriteMealsScreen
import com.example.orangelastsession.ui.screens.meals.MealsScreen
import com.example.orangelastsession.ui.screens.mealsDeatails.MealDetailsScreen
import com.example.orangelastsession.ui.screens.onboarding.OnboardingScreen
import com.example.orangelastsession.ui.screens.profile.ProfileScreen
import com.example.orangelastsession.ui.screens.profile.UpdateProfileInfoScreen
import com.example.orangelastsession.ui.screens.search.SearchScreen
import com.example.orangelastsession.ui.screens.splash.SplashScreen
import com.example.orangelastsession.ui.theme.Orange80

enum class Tabs(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    Meal("Home", Icons.Filled.Home, Screen.MealsScreen.route),
    Search("Search", Icons.Filled.Search, Screen.SearchScreen.route),
    Favorite("Favorite", Icons.Filled.Favorite, Screen.FavoriteScreen.route),
    Profile("Profile", Icons.Filled.Person, Screen.ProfileScreen.route)
}

@Composable
fun MealsNavigationHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
) {

    val hasUser by viewModel.hasUser
    val startDestination = Screen.SplashScreen.route

    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navHostController, hasUser)
        }

        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreen(navController = navHostController)
        }

        composable(route = Screen.LoginScreen.route){
            LoginScreen(nav = navHostController)
        }

        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(nav = navHostController)
        }

        composable(route = Screen.MainScreen.route){
            MainScreen(viewModel, navHostController)
        }
    }
}
@Composable
fun MainNavigationHost(
    modifier: Modifier = Modifier,
    user: User,
    userId: String,
    userName: String,
    userEmail: String,
    viewModel: AuthViewModel,
    navHostController: NavHostController = rememberNavController(),
    navForLogout: NavHostController? = null,
) {

    var displayName by remember { mutableStateOf(userName) }

    LaunchedEffect(userName) {
        displayName = userName
    }

    NavHost(navController = navHostController, startDestination = Tabs.Meal.route) {

        composable(route = Tabs.Meal.route) {
            MealsScreen(navHostController, userId, displayName)
        }

        composable(route = Tabs.Search.route) {
            SearchScreen(
                nav = navHostController,
                userId = userId,
                userName = displayName
            )
        }

        composable(route = Tabs.Favorite.route) {
            FavoriteMealsScreen(
                navController = navHostController,
                userId = userId,
                userName = displayName
            )
        }

        composable(route = Tabs.Profile.route) {
            ProfileScreen(user = user, userName = displayName, userEmail = userEmail, authViewModel = viewModel, navController = navHostController, navForLogout = navForLogout)
        }

        composable(
            route = "${Screen.UpdateProfileInfoScreen.route}?user={user}",
            arguments = listOf(navArgument("user") { NavType.StringType })
        ) {
            UpdateProfileInfoScreen(authViewModel = viewModel, navController = navHostController) {
                navHostController.navigateUp()
            }
        }

        composable(
            route = "${Screen.MealDetailsScreen.route}?meal={meal}",
            arguments = listOf(navArgument("meal") { NavType.StringType })
        ) {
            MealDetailsScreen(nav = navHostController, userId = userId) {
                navHostController.navigateUp()
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    navForLogout: NavHostController? = null
) {
//    val systemUiController = rememberSystemUiController()
//    systemUiController.setSystemBarsColor(
//        color = Color.Transparent,
//        darkIcons = true
//    )
    val networkViewModel: NetworkViewModel = hiltViewModel()
    val isNetworkAvailable by networkViewModel.isNetworkAvailable.collectAsState()

    val userId by viewModel.userId.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val authState = viewModel.authState

    val user by viewModel.user.collectAsState()

    var displayName by remember { mutableStateOf(userName) }

    if (authState.isUserLoggedIn)
        viewModel.getUserInfo()

    LaunchedEffect(userName) {
        displayName = userName
    }

    val navHostController = rememberNavController()
    val backStackState by navHostController.currentBackStackEntryAsState()
    val currentDestination = backStackState?.destination?.route
    val showBottomBar =
        currentDestination == Tabs.Meal.route || currentDestination == Tabs.Search.route
                || currentDestination == Tabs.Favorite.route || currentDestination == Tabs.Profile.route
    val (tabIndex, setTabIndex) = remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(key1 = currentDestination) {
        when (currentDestination) {
            Tabs.Meal.route -> setTabIndex(0)
            Tabs.Search.route -> setTabIndex(1)
            Tabs.Favorite.route -> setTabIndex(2)
            Tabs.Profile.route -> setTabIndex(3)
        }
    }
    val tabs = Tabs.values()

    Scaffold(
        bottomBar = {

            AnimatedVisibility(visible = showBottomBar) {
                AnimatedNavigationBar(
                    tabs = tabs, selectedTabIndex = tabIndex,
                    onTabSelected = { route, index ->
                        setTabIndex(index)
                        navHostController.navigateToBottomBarRoute(route.route)
                    },
                    barColor = Orange80,
                    circleColor = Color.White,
                    selectedColor = Orange80,
                    unselectedColor = Color.White
                )
            }
        },
//        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { paddingValues ->

        if (!isNetworkAvailable) {
            NetworkUnavailableBanner()
        }else {
            MainNavigationHost(
                modifier = Modifier.padding(paddingValues),
                user = user,
                userId = user.userId,
                userName = displayName,
                userEmail = userEmail,
                viewModel = viewModel,
                navHostController = navHostController,
                navForLogout = navForLogout
            )
        }

    }
}

@Composable
fun NetworkUnavailableBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.no_internet_connection),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "Ooops!",
                style = MaterialTheme.typography.headlineMedium,
                color = Orange80,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
            Text(
                text = "No internet connection found. \n Check your connection.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}


fun NavController.navigateToBottomBarRoute(route: String) {
    val currentRoute = currentDestination?.route
    if (route != currentRoute) {
        navigate(route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
        }
    }
}