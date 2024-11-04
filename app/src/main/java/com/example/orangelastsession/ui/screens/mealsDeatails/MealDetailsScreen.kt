package com.example.orangelastsession.ui.screens.mealsDeatails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.ui.components.DetailsBody
import com.example.orangelastsession.ui.components.DetailsTitle
import com.example.orangelastsession.ui.components.MealImageDetails

val BottomBarHeight = 56.dp
val TitleHeight = 128.dp
val GradientScroll = 180.dp
val ImageOverlap = 115.dp
val MinTitleOffset = 56.dp
val MinImageOffset = 12.dp
val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
val ExpandedImageSize = 300.dp
val CollapsedImageSize = 150.dp
val Hzpadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun MealDetailsScreen(
    nav: NavController? = null,
    userId: String,
    viewModel: MealDetailsViewModel = hiltViewModel(),
    upPress: () -> Unit
) {

    val ingredientsList by viewModel.ingredients.collectAsState()
    val ingredientMeals by viewModel.meals.collectAsState()

    val isLoading by viewModel.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        MealDetails(nav = nav, meal = viewModel.meal, userId, ingredientsList, ingredientMeals, viewModel, isLoading, upPress)
    }
}

@Composable
fun MealDetails(
    nav: NavController? = null,
    meal: Meal,
    userId: String,
    ingredientsList: List<String>,
    ingredientMeals: List<Meal>,
    viewModel: MealDetailsViewModel,
    isLoading: Boolean,
    upPress: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(start = 12.dp, end = 12.dp),
    ) {

        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            val scroll = rememberScrollState(0)

            Header()
            DetailsBody(scroll = scroll, meal = meal, nav = nav, ingredientsList = ingredientsList, relatedByIngredient = ingredientMeals, userId = userId, viewModel = viewModel, isLoading = isLoading)
            DetailsTitle(meal = meal) {
                scroll.value
            }
            MealImageDetails(meal.strMealThumb!!) { scroll.value }
            Up(upPress)
//            DetailBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "back"
        )
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
    )
}

//@Composable
//private fun DetailBottomBar(
//    modifier: Modifier = Modifier
//) {
////    var (count, updateCount) = remember {
////        mutableIntStateOf(1)
////    }
//    Surface(
//        modifier
//    ) {
//        Column {
//            Divider()
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .navigationBarsPadding()
//                    .then(Hzpadding)
//                    .heightIn(min = BottomBarHeight)
//            ) {
////                QuantitySelector(
////                    count = count,
////                    decreaseItemCount = { if (count > 0) updateCount(count - 1) },
////                    increaseItemCount = { updateCount(count + 1) })
////                Spacer(modifier = Modifier.width(16.dp))
//                Button(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = "ADD TO FAVORITE",
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        maxLines = 1
//                    )
//                }
//
//            }
//        }
//
//    }
//}