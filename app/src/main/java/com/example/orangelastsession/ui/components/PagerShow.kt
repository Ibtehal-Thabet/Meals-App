package com.example.orangelastsession.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.extensions.fadeInGradientBorder
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.theme.Caramel40
import com.example.orangelastsession.ui.theme.Orange80
import com.google.gson.Gson
import kotlinx.coroutines.delay

@Composable
fun PagerShow(meals: List<Meal>, nav: NavController? = null, modifier: Modifier = Modifier){
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { meals.size })

    val border = Modifier.fadeInGradientBorder(
        showBorder = true,
        colors = listOf(Caramel40, Orange80),
        shape = RoundedCornerShape(10.dp)
    )

    LaunchedEffect(Unit){
        while (true){
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount.coerceAtLeast(1)
            pagerState.scrollToPage(nextPage)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier
                .height(190.dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(20.dp)
                    .then(border),
                elevation = CardDefaults.cardElevation(4.dp)) {

                if (meals.isNotEmpty()) {
                    val mealJson = Gson().toJson(meals[page])
                    Box(modifier = Modifier
                        .weight(1f)
                        .clickable {
                            nav?.navigate("${Screen.MealDetailsScreen.route}?meal=${mealJson}")
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background
//                                Color(0xFF313131)
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = meals[page].strMeal ?: "",
                                fontSize = 16.sp,
                                color = Orange80,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.weight(1f).padding(8.dp)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            AsyncImage(
                                model = meals[page].strMealThumb,
                                contentDescription = meals[page].strMeal,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1.5f)
                                    .clip(RoundedCornerShape(12.dp))
//                                    .padding(4.dp)
                            )
                        }
                    }
                }else{
                    Image(
                        painter = painterResource(id = R.drawable.no_image),
                        contentDescription = "No Image Available",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }

            }
        }

        HorizontalPagerIndicator(
            pagerCount = meals.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
        )
    }
}

@Composable
fun HorizontalPagerIndicator(pagerCount: Int, currentPage: Int, modifier: Modifier.Companion) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pagerCount){
            IndicatorDots(isSelected = it == currentPage, modifier = modifier)
        }
    }
}

@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier.Companion) {
    val size = animateDpAsState(targetValue = if (isSelected) 12.dp else 11.dp, label = "")
    Box(modifier = modifier
        .padding(2.dp)
        .size(size.value)
        .clip(CircleShape)
        .background(if (isSelected) Orange80 else MaterialTheme.colorScheme.onBackground))
}