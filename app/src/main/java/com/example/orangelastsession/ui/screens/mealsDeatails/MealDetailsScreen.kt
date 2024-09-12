package com.example.orangelastsession.ui.screens.mealsDeatails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.entity.meals.Category

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MealDetailsScreen(nav: NavController? = null, viewModel: MealDetailsViewModel = hiltViewModel()){

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)){
        MealDetails(category = viewModel.category)
    }
}

@Composable
fun MealDetails(category: Category){
    Column( modifier = Modifier
        .fillMaxSize()
        .padding(start = 30.dp, end = 30.dp, top = 30.dp),
    ) {

        AsyncImage(
            model = category.strCategoryThumb,
            contentDescription = category.strCategory,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Text(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            , text = category.strCategory ?: "", fontSize = 30.sp, textAlign = TextAlign.Center)
        
    }
}