package com.example.orangelastsession.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.domain.entity.onboarding.OnboardingPage
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.components.HorizontalPagerIndicator
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange80
import com.example.orangelastsession.ui.theme.backgroundDark
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController? = null) {
    val pagerState = rememberPagerState()
//    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(pagerState.currentPage) }
    val gradientColors = listOf(Orange80, Caramel80)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(
                colors = gradientColors,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            ))
            .padding(top = 16.dp, bottom = 16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp,end = 16.dp),
            contentAlignment = Alignment.TopEnd){
            TextButton(onClick = {
                navController?.navigate(route = Screen.LoginScreen.route)
            }) {
                Text(
                    text = "Skip",
                    fontSize = 16.sp,
                    color = Color.White,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Slide in/out to the left when moving forward
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(500)
                        ) + fadeIn(animationSpec = tween(500)) with
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> -fullWidth },
                                    animationSpec = tween(500)
                                ) + fadeOut(animationSpec = tween(500))
                    } else {
                        // Slide in/out to the right when moving backward
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(500)
                        ) + fadeIn(animationSpec = tween(500)) with
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> fullWidth },
                                    animationSpec = tween(500)
                                ) + fadeOut(animationSpec = tween(500))
                    }
                }, label = ""
            ) { targetPage ->
                OnboardingPage(page = onboardingPages[targetPage])
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center) {

            HorizontalPagerIndicator(
                pagerCount = 3,
                currentPage = currentPage,
                modifier = Modifier,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (currentPage < onboardingPages.lastIndex) {
                    currentPage++
                } else {
                    navController?.navigate(route = Screen.LoginScreen.route)
                }
            },
            colors = ButtonColors(containerColor = Color.White, contentColor = Orange80, disabledContainerColor = Color.White, disabledContentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(20.dp)
        ) {
            Text(
                text = if (currentPage == onboardingPages.lastIndex) "Get Started" else "Next",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun OnboardingPage(page: OnboardingPage) {
    val infiniteTransition = rememberInfiniteTransition()
    val scaleAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.decorator_1),
            contentDescription = null,
            modifier = Modifier
                .size(37.dp, 300.dp),
            alignment = Alignment.TopStart
        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .align(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageResId),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(300.dp)
                .scale(scaleAnimation)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontFamily = FontFamily.Serif,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
        Image(
            painter = painterResource(id = R.drawable.decorator_2),
            contentDescription = "",
            modifier = Modifier
                .size(37.dp, 300.dp),
            alignment = Alignment.BottomEnd
        )
    }
}

val onboardingPages = listOf(
    OnboardingPage(
        "Welcome To Chefy",
        "Discover all Meals You Want to Cook",
        R.drawable.chicken
    ),
    OnboardingPage(
        "Easy Search and Save",
        "Search to any Meal, and Add it to Your Favorites to Save it for another Time",
        R.drawable.koshari
    ),
    OnboardingPage(
        "Get Started",
        "Enjoy the app!",
        R.drawable.pizza
    )
)

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GreetingPreview() {
    Column {
        OnboardingScreen()
    }
}
