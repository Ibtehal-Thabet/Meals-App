package com.example.orangelastsession.ui.screens.favorite

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.components.HighlightedMealItem
import com.example.orangelastsession.ui.screens.meals.cardWidthWithPaddingPx
import com.example.orangelastsession.ui.theme.Caramel40
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange40
import com.example.orangelastsession.ui.theme.Orange80
import com.google.gson.Gson


@Composable
fun FavoriteMealsScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController,
    userId: String,
    userName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val rowState = rememberLazyGridState()
    val cardWidthWithPaddingPx = with(LocalDensity.current) {
        cardWidthWithPaddingPx
    }
    val scrollProvide = {
        val offsetFromStart = cardWidthWithPaddingPx * rowState.firstVisibleItemIndex
        offsetFromStart + rowState.firstVisibleItemScrollOffset
    }

    val gradient = when ((0 / 2) % 2) {
        0 -> listOf(Orange80, Caramel40, Caramel80, Orange40)
        else -> listOf(Caramel80, Caramel40, Orange40, Orange80)
    }
//    val scrollState = rememberScrollState()

//    val userId by viewModel.userId.collectAsState()
    val favoriteMeals by viewModel.favoriteMeals.collectAsState()

    // Call the method to fetch favorite meals on screen launch
    LaunchedEffect(userId) {
        viewModel.getAllFavoriteMeals(userId)
    }

    Column(
        modifier = Modifier
//            .verticalScroll(state = scrollState)
            .fillMaxSize()
    ) {

        if (userName.isNotEmpty()) {
            TopText(
                userName.substring(
                    0,
                    userName.indexOf(" ")
                )
            )
        }else{
            TopText("")
        }
        if (favoriteMeals.isEmpty()) {
            EmptyFavoritesScreen()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(favoriteMeals) { index, mealItem ->
                    HighlightedMealItem(
                        mealItem = mealItem,
                        index = index,
                        gradient = gradient,
                        isFavorite = true,
                        onFavoriteClick = {
                            viewModel.removeFromFav(userId = userId, mealId = mealItem.idMeal ?: "")
                            Toast.makeText(
                                context,
                                "Meal removed successfully from Favorites",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        scrollProvider = scrollProvide,
                        onDetailsClick = {
                            val mealJson = Uri.encode(Gson().toJson(mealItem))
                            navController.navigate("MealDetailsScreen?meal=$mealJson")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyFavoritesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.empty_list),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.size(200.dp)
            )
            Text("No favorites yet!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun TopText(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello, $userName",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = "Your Favorite Meals",
                fontSize = 18.sp,
                color = Orange80,
                fontFamily = FontFamily.Serif
            )
        }

//        Box(
//            modifier = Modifier
//                .size(42.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color(0xFFe69e37))
//        )
    }
}

//@Composable
//fun MealItem(
//    meal: Meal,
//    onDetailsClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .clickable { onDetailsClick() },
//        shape = RoundedCornerShape(16.dp),
//        elevation = 8.dp
//    ) {
//        Column(
//            modifier = Modifier.padding(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            // Placeholder image for meal (replace with real meal image)
//            Image(
//                painter = rememberImagePainter(data = meal.strMealThumb),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = meal.strMeal ?: "Meal name",
//                style = MaterialTheme.typography.body1,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GreetingPreview() {
    Column {
//        MealItem(
//            meal = Meal(
//                strMealThumb = painterResource(id = R.drawable.placeholder).toString(),
//                strMeal = "Kabab"
//            )
//        ) {
//
//        }
    }
}

