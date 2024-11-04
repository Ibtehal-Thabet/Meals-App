package com.example.orangelastsession.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.orangelastsession.ui.extensions.GradientAuthBox
import com.example.orangelastsession.ui.extensions.rememberImeState
import com.example.orangelastsession.ui.navigation.Tabs
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.screens.auth.AuthViewModel
import com.example.orangelastsession.ui.theme.OrangeLastSessionTheme
import com.example.orangelastsession.ui.theme.backgroundDark

@Composable
fun SignUpScreen(nav: NavController? = null, viewModel: AuthViewModel = hiltViewModel()){

    val context = LocalContext.current
    val isImeVisible by rememberImeState()

    val authState = viewModel.authState
    val isError = authState.signUpError != null
    val hasUser by viewModel.hasUser

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }


    GradientAuthBox(
        modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val animatedUpperSectionRatio by animateFloatAsState(
                targetValue = if (isImeVisible) 0f else 0.30f,
                label = ""
            )

            AnimatedVisibility(visible = !isImeVisible, enter = fadeIn(), exit = fadeOut()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(animatedUpperSectionRatio),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Black.weight,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isError) {
                    Text(text = authState.signUpError ?: "Unknown Error", color = Color.Red)
                }

                Spacer(modifier = Modifier.fillMaxSize(0.05f))

                Text(
                    text = "SignUp",
                    style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Black.weight,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp),
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = authState.userNameSignUp,
                    onValueChange = { viewModel.onUserNameSignUpChange(it) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    label = {
                        Text(text = "Name")
                    },
                    isError = isError
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = authState.userEmailSignUp,
                    onValueChange = { viewModel.onUserEmailSignUpChange(it) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    },
                    label = {
                        Text(text = "Email")
                    },
                    isError = isError
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = authState.passwordSignUp ?: "",
                    onValueChange = { viewModel.onPasswordSignUpChange(it) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = {
                        Text(text = "Password")
                    },
//                    visualTransformation = PasswordVisualTransformation(),
                    isError = isError
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = authState.confirmPasswordSignUp ?: "",
                    onValueChange = { viewModel.onConfirmPasswordSignUpChange(it) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = {
                        Text(text = "Password Confirmation")
                    },
//                    visualTransformationmation = PasswordVisualTransformation(),
                    isError = isError
                )

                if (authState.isLoading) {
                    CircularProgressIndicator()
                }else {
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = { viewModel.createUser(context) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "SignUp")
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Already have an Account?", color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.size(4.dp))
                    TextButton(onClick = { nav?.navigate(Screen.LoginScreen.route) {
                        popUpTo(route = Screen.SignUpScreen.route) {
                            inclusive = true
                        }
                    }
                    }) {
                        Text(text = "Login")
                    }
                }

                LaunchedEffect(key1 = hasUser) {
                    if (hasUser) {
                        nav?.navigate(Screen.LoginScreen.route){
                            launchSingleTop = true
                            popUpTo(route = Screen.SignUpScreen.route){
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpScreen(){
    OrangeLastSessionTheme {
        GradientAuthBox(
            modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

//                val animatedUpperSectionRatio by animateFloatAsState(
//                    targetValue = if (isImeVisible) 0f else 0.35f,
//                    label = ""
//                )
//
//                AnimatedVisibility(visible = !isImeVisible, enter = fadeIn(), exit = fadeOut()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.30f),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Black.weight,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.fillMaxSize(0.05f))

                    Text(
                        text = "SignUp",
                        style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Black.weight,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 16.dp),
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = "authState.userNameSignUp",
                        onValueChange = {
//                            viewModel.onUserNameSignUpChange(it)
                                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        label = {
                            Text(text = "Name")
                        },
//                        isError = isError
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = "",
                        onValueChange = { },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        label = {
                            Text(text = "Email")
                        },
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = "",
                        onValueChange = { },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        },
                        label = {
                            Text(text = "Password")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = "",
                        onValueChange = { },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                        },
                        label = {
                            Text(text = "Password Confirmation")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = { },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "SignUp")
                    }
                    Spacer(modifier = Modifier.size(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "Already have an Account?", color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.size(4.dp))
                        TextButton(onClick = { }) {
                            Text(text = "Login")
                        }
                    }
                }
            }
        }
    }
}