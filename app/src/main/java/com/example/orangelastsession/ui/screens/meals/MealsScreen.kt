package com.example.orangelastsession.ui.screens.meals

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domain.entity.meals.Meal
import com.example.domain.entity.meals.MealResponse
import com.example.orangelastsession.ui.components.FilterList
import com.example.orangelastsession.ui.components.HighlightedMealItem
import com.example.orangelastsession.ui.components.PagerShow
import com.example.orangelastsession.ui.extensions.ShimmerLoading
import com.example.orangelastsession.ui.extensions.ShimmerLoadingForCategories
import com.example.orangelastsession.ui.extensions.ShimmerLoadingForPager
import com.example.orangelastsession.ui.extensions.filters
import com.example.orangelastsession.ui.screens.Screen
import com.example.orangelastsession.ui.theme.Caramel40
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange40
import com.example.orangelastsession.ui.theme.Orange80
import com.google.gson.Gson

@Composable
fun MealsScreen(nav: NavController? = null, userId: String, userName: String, viewModel: MealsViewModel = hiltViewModel()){
    val randomMeals by viewModel.randomMeal.collectAsState()
    val meals by viewModel.meals.collectAsState()

    MealsScreenComponents(nav, userId, userName, viewModel, randomMeals, meals)

}

@Composable
fun MealsScreenComponents(
    nav: NavController?, userId: String, userName: String,
    viewModel: MealsViewModel, randomMeals: MealResponse, meals: List<Meal>
){

    val isLoadingCategories by viewModel.isLoadingCategories
    val isLoading by viewModel.isLoading
    val isLoadingPager by viewModel.isLoadingPager

//    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
//            .verticalScroll(state = scrollState)
            .fillMaxSize()
    ) {
        if (userName.isNotEmpty()) {
            TopText(userName.substring(0, userName.indexOf(" ")))
        }else{
            TopText("")
        }

        if (isLoadingPager) {
            ShimmerLoadingForPager()
        } else {
            PagerShow(meals = randomMeals.meals, nav = nav)
        }

        Spacer(modifier = Modifier.padding(8.dp))

        if (isLoadingCategories){
            ShimmerLoadingForCategories()
        }else {
            FilterList(filters = filters, viewModel = viewModel){selectedFilter ->
                when(selectedFilter.name){
                    "Category" -> viewModel.getMealsByCategory("Beef")
                    "Area" -> viewModel.getMealsByArea("American")
                    "Letters" -> viewModel.getMealsByLetter("A")
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        if (isLoading) {
            ShimmerLoading()
        } else {
            if (meals.isNotEmpty())
                HighlightedMeals(userId = userId, index = 0, mealItems = meals, viewModel = viewModel, nav = nav)
        }

        Spacer(modifier = Modifier.padding(8.dp))
    }
}

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp
val Density.cardWidthWithPaddingPx
    get() = (HighlightCardWidth + HighlightCardPadding).toPx()
@Composable
fun HighlightedMeals(
    userId: String,
    index: Int,
    mealItems: List<Meal>,
    viewModel: MealsViewModel,
    nav: NavController? = null,
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

    val gradient = when ((index / 2) % 2) {
        0 -> listOf(Orange80, Caramel40, Caramel80, Orange40)
        else -> listOf(Caramel80, Caramel40, Orange40, Orange80)
    }

    val favoriteMeals by viewModel.favoriteMeals.collectAsState()

    LaunchedEffect(mealItems) {
        if (userId.isNotEmpty())
            viewModel.loadFavoriteMeals(userId, mealItems)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = rowState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 50.dp),
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
                    }},
                scrollProvider = scrollProvide,
                onDetailsClick = { nav?.navigate("${Screen.MealDetailsScreen.route}?meal=${mealJson}") }
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
                text = "What are you cooking Today?",
                fontSize = 16.sp,
                color = Orange80,
                fontFamily = FontFamily.SansSerif
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GreetingPreview() {
    Column {
//        PagerShow(listOf(
//            Meal(strMealThumb = "", strCategory = "Meat"),
//            Meal(strMealThumb = "", strCategory = "Chicken")
//        ))
//        CategoriesList(
//            listOf(
//                Category(strCategoryThumb = "", strCategory = "Meat"),
//                Category(strCategoryThumb = "", strCategory = "Chicken")
//            )
//        )
        Spacer(modifier = Modifier.padding(8.dp))
//        HighlightedMeals(0, listOf(Meal(strMealThumb = painterResource(id = R.drawable.placeholder).toString(), strMeal = "Kabab"),
//            Meal(strMealThumb = painterResource(id = R.drawable.placeholder).toString(), strMeal = "Shawerma"),
//            Meal(strMealThumb = painterResource(id = R.drawable.placeholder).toString(), strMeal = "Kabab"),
//            Meal(strMealThumb = painterResource(id = R.drawable.placeholder).toString(), strMeal = "Shawerma")))
        Spacer(modifier = Modifier.padding(8.dp))
//        MealsList(listOf(Meal(strMealThumb = painterResource(id = R.drawable.placeholder).toString(), strMeal = "Kabab", strTags = "KATAG"), Meal(strMealThumb = painterResource(id = R.drawable.placeholder).toString(), strMeal = "Shawerma", strTags = "SHAW")))
        Spacer(modifier = Modifier.padding(8.dp))
    }
}