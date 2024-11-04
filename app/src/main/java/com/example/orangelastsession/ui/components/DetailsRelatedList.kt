package com.example.orangelastsession.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.ui.extensions.ShimmerLoadingForDetails
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.screens.mealsDeatails.MealDetailsViewModel
import com.example.orangelastsession.ui.theme.Caramel40
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange40
import com.example.orangelastsession.ui.theme.Orange80
import com.google.gson.Gson

@Composable
fun DetailsMealsRelated(
    modifier: Modifier = Modifier,
    nav: NavController? = null,
    meals: List<Meal>,
    userId: String,
    viewModel: MealDetailsViewModel,
    isLoading: Boolean){

    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Related Meals",
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = FontFamily.Cursive,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
//            IconButton(
//                onClick = { /*TODO*/ },
//                modifier = Modifier.align(
//                    Alignment.CenterVertically
//                ),
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
//                    contentDescription = null
//                )
//            }
        }
        if (isLoading){
            ShimmerLoadingForDetails()
        }else {
            if (meals.isNotEmpty()) {
                HighlightedMeals(
                    index = 0,
                    mealItems = meals,
                    userId = userId,
                    nav = nav,
                    viewModel = viewModel
                )
            }
        }
    }
}

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp
private val Density.cardWidthWithPaddingPx
    get() = (HighlightCardWidth + HighlightCardPadding).toPx()
@Composable
fun HighlightedMeals(
    index: Int,
    mealItems: List<Meal>,
    userId: String,
    viewModel: MealDetailsViewModel,
    nav: NavController? = null,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val rowState = rememberLazyListState()
    val cardWidthWithPaddingPx = with(LocalDensity.current) {
        cardWidthWithPaddingPx
    }
    val scrollProvide = {
        val offsetFromStart = cardWidthWithPaddingPx * rowState.firstVisibleItemIndex
        offsetFromStart + rowState.firstVisibleItemScrollOffset
    }

    val gradient = when ((index / 2) % 2) {
        0 -> listOf(Orange80, Caramel40, Caramel80, Orange40)
        else -> listOf(Caramel80, Caramel40, Orange40, Orange80)
    }

    val favoriteMeals by viewModel.favoriteMeals.collectAsState()

    // Fetch favorite meals only once when the mealItems are loaded
    LaunchedEffect(mealItems) {
        viewModel.loadFavoriteMeals(userId, mealItems)
    }

    LazyRow(
        state = rowState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
    ) {
        itemsIndexed(mealItems) { index, mealItem ->
            val mealJson = Gson().toJson(mealItem)

            val favMeal = favoriteMeals[mealItem.idMeal ?: ""] ?: false
            var isFavorite by remember { mutableStateOf(favMeal) }

            LaunchedEffect(favMeal) {
                isFavorite = favMeal
            }
            HighlightedMealItem(
                mealItem = mealItem,
                index = index,
                gradient = gradient,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    isFavorite = !isFavorite
                    if (isFavorite){
                        viewModel.addToFav(userId = userId, meal = mealItem)
                        Toast.makeText(context, "Meal added successfully to Favorites", Toast.LENGTH_SHORT).show()
                    } else{
                        viewModel.removeFomFav(userId = userId, meal = mealItem)
                        Toast.makeText(context, "Meal removed successfully from Favorites", Toast.LENGTH_SHORT).show()
                    }
                } ,
                scrollProvider = scrollProvide,
                onDetailsClick = { nav?.navigate("${Screen.MealDetailsScreen.route}?meal=${mealJson}") }
            )
        }
    }
}