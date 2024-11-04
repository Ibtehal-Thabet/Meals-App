package com.example.orangelastsession.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.theme.Orange80
import com.google.gson.Gson

@Composable
fun SearchScreen(
    nav: NavController?, userId: String,
    userName: String,
    viewModel: SearchViewModel = hiltViewModel()
){

    val isSearching by viewModel.isSearching
    val searchText by viewModel.searchText.collectAsState()
    val searchedMeals by viewModel.searchMealList.collectAsState()

    Column(
        modifier = Modifier
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
        Spacer(modifier = Modifier.padding(6.dp))
        SearchTextField(searchText = searchText, viewModel = viewModel)
        Spacer(modifier = Modifier.padding(16.dp))

        if (searchText.isNotEmpty() && searchedMeals.isNullOrEmpty()){
            Image(
                painter = painterResource(id = R.drawable.no_search_result),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.size(250.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = "No Searched Meals!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

        } else if (searchedMeals.isNullOrEmpty()){
                Text(
                    text = "No Meals",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
        }else{
            if (isSearching) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }else {
                SearchMealList(searchedMeals = searchedMeals, nav)
            }
        }
    }
}

@Composable
private fun SearchTextField(searchText: String, viewModel: SearchViewModel){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = searchText,
        onValueChange =  { newText ->
            viewModel.onSearchTextChange(newText)
            viewModel.searchMeals()
        },
        label = {
            Text(text = "Search Meals")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
    )
}

@Composable
private fun SearchMealList(searchedMeals: List<Meal>, nav: NavController? = null){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 50.dp)){
        items(searchedMeals){meal ->
            val mealJson = Gson().toJson(meal)
            SearchMealItem(meal){
                nav?.navigate("MealDetailsScreen?meal=${mealJson}")
            }
        }
    }
}

@Composable
private fun SearchMealItem(meal: Meal, onDetailsClick: () -> Unit){
    Card(
        modifier = Modifier
            .clickable(
                onClick = {
                    onDetailsClick()
                }
            )
            .padding(8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
        ) {
            MealSearchImage(
                imageUrl = meal.strMealThumb ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterVertically)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = meal.strMeal ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "",
                        tint = Orange80,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = meal.strArea ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

}

@Composable
fun MealSearchImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    Surface(
        color = Color.LightGray,
        shadowElevation = elevation,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            placeholder = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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