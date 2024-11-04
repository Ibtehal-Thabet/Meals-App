package com.example.orangelastsession.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.meals.Meal
import com.example.orangelastsession.ui.screens.mealsDeatails.Hzpadding
import com.example.orangelastsession.ui.screens.mealsDeatails.MaxTitleOffset
import com.example.orangelastsession.ui.screens.mealsDeatails.MinTitleOffset
import com.example.orangelastsession.ui.screens.mealsDeatails.TitleHeight
import com.example.orangelastsession.ui.theme.Caramel80

@Composable
fun DetailsTitle(
    meal: Meal,
    scrollProvider: () -> Int
) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = meal.strMeal ?: "",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Hzpadding,
        )
        Row(
            modifier = Hzpadding,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "",
                tint = Caramel80
            )
            Text(
                text = meal.strArea!!,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 20.sp
            )
        }
//        Spacer(modifier = Modifier.height(4.dp))
    }
    Spacer(modifier = Modifier.height(4.dp))
//        IconButton(
//            onClick = {},
//            modifier = Hzpadding
//        ) {
//            Icon(
//                imageVector = Icons.Filled.FavoriteBorder,
//                contentDescription = "back",
//                tint = Orange80
//            )
//        }
}