package com.example.orangelastsession.ui.components

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.screens.mealsDeatails.BottomBarHeight
import com.example.orangelastsession.ui.screens.mealsDeatails.GradientScroll
import com.example.orangelastsession.ui.screens.mealsDeatails.Hzpadding
import com.example.orangelastsession.ui.screens.mealsDeatails.ImageOverlap
import com.example.orangelastsession.ui.screens.mealsDeatails.MealDetailsViewModel
import com.example.orangelastsession.ui.screens.mealsDeatails.MinTitleOffset
import com.example.orangelastsession.ui.screens.mealsDeatails.TitleHeight
import com.example.orangelastsession.ui.theme.Orange80

@Composable
fun DetailsBody(
    scroll: ScrollState,
    meal: Meal,
    nav: NavController? = null,
    ingredientsList: List<String>,
    relatedByIngredient: List<Meal>,
    userId: String,
    viewModel: MealDetailsViewModel,
    isLoading: Boolean
) {

    val context = LocalContext.current
    val isFavoriteMeal by viewModel.isFavorite.collectAsState()

    // Fetch favorite meals only once when the mealItems are loaded
    LaunchedEffect(isFavoriteMeal) {
        viewModel.isFavoriteMeal(userId, meal)
    }

    var isFavorite by remember { mutableStateOf(isFavoriteMeal) }

    LaunchedEffect(isFavoriteMeal) {
        isFavorite = isFavoriteMeal
    }

    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
                .background(MaterialTheme.colorScheme.background)
        )
        Column(
            modifier = Modifier
                .verticalScroll(scroll)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(GradientScroll))
            Surface(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Spacer(modifier = Modifier.height(ImageOverlap))
                    Spacer(modifier = Modifier.height(TitleHeight))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        meal.strYoutube?.let {
                            val logoPainter = painterResource(id = R.drawable.youtube_logo)
                            YouTubeLink(it, logoPainter = logoPainter)
                        }
                        Spacer(Modifier.weight(1f).fillMaxHeight())
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                if (isFavorite){
                                    viewModel.addToFav(userId = userId, meal = meal)
                                    Toast.makeText(context, "Meal added successfully to Favorites", Toast.LENGTH_SHORT).show()
                                } else{
                                    viewModel.removeFomFav(userId = userId, meal = meal)
                                    Toast.makeText(context, "Meal removed successfully from Favorites", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)

                        ){
                            Icon(imageVector = if (isFavorite) Icons.Filled.Favorite
                            else Icons.Outlined.FavoriteBorder,
                                contentDescription = "",
                                tint = Orange80,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = FontFamily.Monospace,
                        color = Orange80,
                        modifier = Hzpadding
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    var seeMore by remember {
                        mutableStateOf(true)
                    }

                    val ingredientsText = ingredientsList.joinToString(separator = "\n")

                    Text(
                        text = ingredientsText,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Hzpadding
                    )
                    if (!seeMore) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = FontFamily.Monospace,
                            color = Orange80,
                            modifier = Hzpadding
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = meal.strInstructions ?: "",
                            style = MaterialTheme.typography.bodyMedium,
//                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Hzpadding,

                            )
                    }
                    val textBtn = if (seeMore) "See More" else "See less"
                    Text(
                        text = textBtn,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Divider()
//                    relatedByIngredient.forEach { areaMeal ->
//                        key(areaMeal.idMeal) {
                    DetailsMealsRelated(meals = relatedByIngredient, userId = userId, nav = nav,viewModel = viewModel, isLoading = isLoading)
//                        }
//                    }
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }

}