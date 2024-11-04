package com.example.orangelastsession.ui.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp


@Composable
fun ShimmerLoading() {
    // Define the shimmer effect with an infinite transition
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ), label = ""
    )

    // Create a grid of placeholders
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Number of columns
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        items(6) { // Specify number of items to show (e.g., 6 placeholders)
            ShimmerItem(translateAnim) // Show shimmer item
        }
    }
}

@Composable
fun ShimmerLoadingForDetails() {
    // Define the shimmer effect with an infinite transition
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ), label = ""
    )

    // Create a grid of placeholders
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        items(6) { // Specify number of items to show (e.g., 6 placeholders)
            ShimmerItem(translateAnim) // Show shimmer item
        }
    }
}

@Composable
fun ShimmerItem(translateAnim: Float) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Gray, Color.LightGray, Color.Gray),
                    start = Offset(translateAnim * 1000f, 0f),
                    end = Offset(translateAnim * 1000f + 200f, 0f)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .aspectRatio(1f)
            .size(150.dp), // Keeps a square shape for grid items
    )
}


@Composable
fun ShimmerLoadingForPager() {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ), label = ""
    )
    // Shimmering Box
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(36.dp)
            .height(190.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Gray, Color.LightGray, Color.Gray),
                    start = Offset(translateAnim * 1000f, 0f),
                    end = Offset(translateAnim * 1000f + 200f, 0f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
    )
}

@Composable
fun ShimmerLoadingForCategories() {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ), label = ""
    )
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(6) {
            // Shimmering Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .shadow(shape = CircleShape, elevation = 5.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Gray, Color.LightGray, Color.Gray),
                            start = Offset(translateAnim * 1000f, 0f),
                            end = Offset(translateAnim * 1000f + 200f, 0f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.google.accompanist.placeholder.material.placeholder
//import com.google.accompanist.shimmer.shimmer
//
//@Composable
//fun ShimmerLoading() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Column {
//            // Shimmering Text
//            Box(
//                modifier = Modifier
//                    .height(20.dp)
//                    .fillMaxWidth(0.7f)
//                    .background(Color.Gray)
//                    .shimmer()
//                    .placeholder(Color.LightGray)
//                    .padding(4.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Shimmering Box
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .background(Color.Gray)
//                    .shimmer()
//                    .placeholder(Color.LightGray)
//                    .padding(8.dp)
//            )
//        }
//    }
//}
