package com.example.orangelastsession.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.domain.entity.auth.User
import com.example.orangelastsession.ui.components.ThemeSwitcher
import com.example.orangelastsession.ui.navigation.Tabs
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.screens.auth.AuthViewModel
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange80
import com.example.orangelastsession.ui.theme.OrangeLastSessionTheme
import com.google.gson.Gson

@Composable
fun ProfileScreen(
    user: User,
    userName: String,
    userEmail: String,
    viewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    navController: NavHostController? = null,
    navForLogout: NavHostController? = null,
) {

    val context = LocalContext.current
    val darkTheme by viewModel.isDarkTheme.collectAsState()

    var name by remember { mutableStateOf(userName) }
    LaunchedEffect(userName) {
        name = userName
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // User Name
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .padding(bottom = 8.dp),
                color = Orange80
            )
            Spacer(modifier = Modifier.height(12.dp))

            // User Email
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyLarge,
                color = Caramel80
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextButtonWithIcon(
                text = "Your Favorites",
                textColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    navController?.navigate(Tabs.Favorite.route){
                        launchSingleTop = true
                        popUpTo(route = Tabs.Profile.route){
                            inclusive = true
                        }
                    }
                          },
                icon = Icons.Filled.Favorite,
                iconLeft = Icons.AutoMirrored.Filled.ArrowForwardIos,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val userJson = Gson().toJson(user)
            // update user profile
            TextButtonWithIcon(
                text = "Profile",
                textColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    navController?.navigate("${Screen.UpdateProfileInfoScreen.route}?user=${userJson}")
                },
                icon = Icons.Filled.Person,
                iconLeft = Icons.AutoMirrored.Filled.ArrowForwardIos,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ModeEdit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                ThemeSwitcher(
                    darkTheme = darkTheme,
                    size = 30.dp,
                    padding = 5.dp,
                    onClick = {
                        viewModel.toggleTheme()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // logout Button
            TextButton(onClick = {
                authViewModel.logoutUser(context)
                navForLogout?.navigate(Screen.LoginScreen.route){
                    launchSingleTop = true
                    popUpTo(route = Screen.MainScreen.route){
                        inclusive = true
                    }
                }
            }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                }

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "Version 3.2",
                style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .padding(bottom = 56.dp),
                color = Orange80
            )
        }
    }
}

@Composable
fun TextButtonWithIcon(
    text: String,
    textColor: Color,
    onClick: () -> Unit,
    icon: ImageVector,
    iconLeft: ImageVector,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = iconLeft,
            contentDescription = null,
            modifier = Modifier
                .size(18.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GreetingPreview() {
    OrangeLastSessionTheme {
//        ProfileScreen(
//            User(),
//            userName = "Ibtehal",
//            userEmail = "ibtehal12@gmail.com",
//        )
    }
}