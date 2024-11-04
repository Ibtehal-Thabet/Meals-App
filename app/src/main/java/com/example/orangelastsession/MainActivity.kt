package com.example.orangelastsession

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orangelastsession.ui.extensions.BarShape
import com.example.orangelastsession.ui.navigation.MealsNavigationHost
import com.example.orangelastsession.ui.navigation.Tabs
import com.example.orangelastsession.ui.screens.profile.ProfileViewModel
import com.example.orangelastsession.ui.theme.Orange80
import com.example.orangelastsession.ui.theme.OrangeLastSessionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val themeViewModel: ProfileViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            OrangeLastSessionTheme(darkTheme = isDarkTheme) {
                val systemBars = WindowInsets.systemBars
                val paddingValues = systemBars.asPaddingValues()

                Box(
                    modifier = Modifier.fillMaxSize()
//                        .statusBarsPadding()
//                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(paddingValues.calculateTopPadding())
                            .background(MaterialTheme.colorScheme.background)
                    )

                    // Draw the navigation bar background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(paddingValues.calculateBottomPadding())
                            .background(MaterialTheme.colorScheme.background) // Customize this background color
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        MealsNavigationHost()
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedNavigationBar(
    tabs: Array<Tabs>,
    selectedTabIndex: Int,
    onTabSelected: (route: Tabs, index: Int) -> Unit,
    barColor: Color,
    circleColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
) {
    val circleRadius = 26.dp

    var barSize by remember { mutableStateOf(IntSize(0, 0)) }
    // first item's center offset for Arrangement.SpaceAround
    val offsetStep = remember(barSize) {
        barSize.width.toFloat() / (tabs.size * 2)
    }
    val offset = remember(selectedTabIndex, offsetStep) {
        offsetStep + selectedTabIndex * 2 * offsetStep
    }
    val circleRadiusPx = LocalDensity.current.run { circleRadius.toPx().toInt() }
    val offsetTransition = updateTransition(offset, "offset transition")
    val animation = spring<Float>(dampingRatio = 0.5f, stiffness = Spring.StiffnessVeryLow)
    val cutoutOffset by offsetTransition.animateFloat(
        transitionSpec = {
            if (this.initialState == 0f) {
                snap()
            } else {
                animation
            }
        },
        label = "cutout offset"
    ) { it }
    val circleOffset by offsetTransition.animateIntOffset(
        transitionSpec = {
            if (this.initialState == 0f) {
                snap()
            } else {
                spring(animation.dampingRatio, animation.stiffness)
            }
        },
        label = "circle offset"
    ) {
        IntOffset(it.toInt() - circleRadiusPx * 8, -circleRadiusPx)
    }
    val barShape = remember(cutoutOffset) {
        BarShape(
            offset = cutoutOffset,
            circleRadius = circleRadius,
            cornerRadius = 25.dp,
        )
    }

    Box(
        modifier = Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter) {
        Circle(
            modifier = Modifier
                .offset { circleOffset }
                .border(width = 1.dp, color = Orange80, shape = CircleShape)
                // the circle should be above the bar for accessibility reasons
                .zIndex(1f),
            color = circleColor,
            radius = circleRadius,
            tab = tabs[selectedTabIndex],
            iconColor = selectedColor,
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .onPlaced { barSize = it.size }
                .graphicsLayer {
                    shape = barShape
                    clip = true
                }
                .fillMaxWidth()
                .background(barColor),
            containerColor = barColor
        ) {
            tabs.forEachIndexed { index, route ->
                val isSelected = index == selectedTabIndex
                Tab(
                    selected = isSelected,
                    onClick = {
                        onTabSelected(route, index)
                              },
                    icon = {
                        val iconAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 0f else 1f,
                            label = "Navbar item icon"
                        )
                        Icon(
                            imageVector = route.icon,
                            contentDescription = route.title,
                            modifier = Modifier.alpha(iconAlpha)
                        )
                    },
                    selectedContentColor = selectedColor,
                    unselectedContentColor = unselectedColor,
                )
            }
        }
    }
}


@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    radius: Dp,
    tab: Tabs,
    iconColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2)
            .clip(CircleShape)
            .background(color),
    ) {
        AnimatedContent(
            targetState = tab.icon, label = "Bottom bar circle icon",
        ) { targetIcon ->
            Icon(targetIcon, tab.title, tint = iconColor)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    OrangeLastSessionTheme {
        MealsNavigationHost()
    }
}