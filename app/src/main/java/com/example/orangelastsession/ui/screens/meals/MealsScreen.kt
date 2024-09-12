package com.example.orangelastsession.ui.screens.meals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.entity.meals.Category
import com.google.gson.Gson

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MealsScreen(nav: NavController? = null, viewModel: MealsViewModel = hiltViewModel()){
    val category by viewModel.category.collectAsState()

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 30.dp, bottom = 30.dp, start = 10.dp, end = 10.dp), verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.Start){
        items(category.categories){
            val categoryJson = Gson().toJson(it)
            ItemMeal(category = it, action = { nav?.navigate("MealDetailsScreen?category=${categoryJson}") })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemMeal(category: Category, modifier: Modifier = Modifier, action :()->Unit) {
    Card(
        onClick = action,
        modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AsyncImage(
                model = category.strCategoryThumb,
                contentDescription = category.strCategory,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Text(text = category.strCategory ?: "")

        }

    }
}