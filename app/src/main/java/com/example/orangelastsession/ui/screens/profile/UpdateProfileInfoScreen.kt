package com.example.orangelastsession.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.domain.entity.auth.User
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.screens.auth.AuthViewModel
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange80
import com.example.orangelastsession.ui.theme.backgroundDark


@Composable
fun UpdateProfileInfoScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    navController: NavHostController? = null,
    navForLogout: NavHostController? = null,
    upPress: () -> Unit
) {
    val context = LocalContext.current

    var isNameUpdating by remember { mutableStateOf(false) }
    var isPasswordUpdating by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val user = viewModel.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
            .verticalScroll(rememberScrollState()),
    ) {

//        Box(modifier = Modifier
//            .fillMaxSize()
//            ) {
            Header(upPress)

        Spacer(modifier = Modifier.height(28.dp))
            // update user name
            TextButtonWithIcon(
                text = "Update Name",
                textColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    isNameUpdating = !isNameUpdating
                },
                icon = Icons.Filled.Person,
                iconLeft = if (isNameUpdating) Icons.Filled.KeyboardArrowDown else Icons.AutoMirrored.Filled.ArrowForwardIos,
                modifier = Modifier.fillMaxWidth()
            )

            if (isNameUpdating) {
                EditProfile(userName = user.userName, viewModel = viewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // change password
            TextButtonWithIcon(
                text = "Change Password",
                textColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    isPasswordUpdating = !isPasswordUpdating
                },
                icon = Icons.Filled.Lock,
                iconLeft = if (isPasswordUpdating) Icons.Filled.KeyboardArrowDown else Icons.AutoMirrored.Filled.ArrowForwardIos,
                modifier = Modifier.fillMaxWidth()
            )

            if (isPasswordUpdating) {
                ChangePassword(viewModel = viewModel, authViewModel = authViewModel, navForLogout)
            }
        Spacer(modifier = Modifier.height(16.dp))

        // delete account
        TextButtonWithIcon(
            text = "Delete Your Account",
            textColor = Color.Red,
            onClick = {
                showDialog = true
            },
            icon = Icons.Filled.Delete,
            iconLeft = Icons.AutoMirrored.Filled.ArrowForwardIos,
            modifier = Modifier.fillMaxWidth()
        )

        if (showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirm Deletion", color = Color.Red) },
                text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.deleteUserAccount(context = context)
                    }) {
                        Text(text = "Confirm", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
//        }
    }

}


@Composable
fun EditProfile(userName: String, viewModel: ProfileViewModel){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val isLoading by viewModel.isLoading
    var newName by remember { mutableStateOf(userName) }
    LaunchedEffect(userName) {
//        if (!isLoading) {
        newName = userName
//        }
        Log.i("profile user name", "$newName, $userName")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            value = newName,
            onValueChange = {
                newName = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = {
                Text(text = "New Name")
            }
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.updateUserName(context, newName)
            },
            enabled = newName.isNotBlank() && newName!=userName && !isLoading,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if(!isLoading) "Save Changes" else "Saving...", color = Color.White)
        }
    }
}

@Composable
fun ChangePassword(viewModel: ProfileViewModel, authViewModel: AuthViewModel, navForLogout: NavHostController?){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isLoading by viewModel.isLoading

    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmNewPass by remember { mutableStateOf("") }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmNewPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            value = currentPass,
            onValueChange = {
                currentPass = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                    Icon(
                        imageVector = if (currentPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (currentPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = {
                Text(text = "Current Password")
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            value = newPass,
            onValueChange = {
                newPass = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                    Icon(
                        imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (newPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = {
                Text(text = "New Password")
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            value = confirmNewPass,
            onValueChange = {
                confirmNewPass = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { confirmNewPasswordVisible = !confirmNewPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmNewPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmNewPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (confirmNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = {
                Text(text = "Confirm New Password")
            }
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                authViewModel.logoutUser(context)
                viewModel.changePassword(context, currentPass, newPass, confirmNewPass)
                navForLogout?.navigate(Screen.LoginScreen.route){
                    launchSingleTop = true
                    popUpTo(route = Screen.UpdateProfileInfoScreen.route){
                        inclusive = true
                    }
                }
            },
            enabled = currentPass.isNotBlank() && newPass.isNotBlank() && confirmNewPass.isNotBlank()
                    && newPass!=(currentPass),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if(!isLoading) "Change Password" else "Changing...", color = Color.White)
        }
    }
}


@Composable
fun Header(upPress: () -> Unit) {
    Row(
        modifier = Modifier
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = upPress,
            modifier = Modifier
                .size(36.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "back"
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(text = "Profile",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DarkGradientBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Orange80.copy(alpha = 0.8f),
                        Caramel80.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.background
                    ),
//                    start = Offset(0f, 0f),
//                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        // screen content here
    }
}