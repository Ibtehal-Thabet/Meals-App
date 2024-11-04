package com.example.orangelastsession.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.orangelastsession.R
import com.example.orangelastsession.ui.extensions.Filter
import com.example.orangelastsession.ui.extensions.diagonalGradientBorder
import com.example.orangelastsession.ui.extensions.fadeInGradientBorder
import com.example.orangelastsession.ui.screens.meals.MealsViewModel
import com.example.orangelastsession.ui.theme.Caramel40
import com.example.orangelastsession.ui.theme.Orange80
import com.example.orangelastsession.ui.theme.backgroundDark
import com.example.orangelastsession.ui.theme.primaryLight

@Composable
fun FilterList(filters: List<Filter>, viewModel: MealsViewModel, onFilterSelected: (Filter)->Unit){

    var isFilterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(filters.firstOrNull()) }
    var selectedFilterItem by remember { mutableStateOf(selectedFilter) }

    LaunchedEffect(selectedFilter) {
        selectedFilter?.let {
            when (it.name){
                "Category" -> viewModel.getMealsByCategory("Beef")
                "Area" -> viewModel.getMealsByArea("American")
                "Letters" -> viewModel.getMealsByLetter("A")
            }
        }
        selectedFilterItem = selectedFilter
    }

//    LaunchedEffect(selectedFilterItem) {
//        selectedFilterItem = selectedFilter
//    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        selectedFilter?.let { filter ->
            when (filter.name) {
                "Category" -> {
                    val categories by viewModel.categories.collectAsState()

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterCard(
                                filter = Filter(name = category.strCategory?:"", icon = category.strCategoryThumb?:""),
                                filterName = filter.name,
                                filterIndex = 0,
                                isSelected = selectedFilterItem?.name == category.strCategory, // Example selection state
                                onClick = {
                                    selectedFilterItem = Filter(name = category.strCategory ?: "Beef")
                                    viewModel.getMealsByCategory(category.strCategory?:"Beef")
                                }
                            )
                        }
                    }
                }
                "Area" -> {
                    viewModel.getAreas()
                    val areas by viewModel.areas.collectAsState()
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(areas) { area ->
                            FilterCard(
                                filter = Filter(name = area.strArea?:""),
                                filterName = filter.name,
                                filterIndex = areas.indexOf(area),
                                isSelected = selectedFilterItem?.name == area.strArea,
                                onClick = {
                                    selectedFilterItem = Filter(name = area.strArea?:"American")
                                    viewModel.getMealsByArea(area.strArea?:"American")
                                }
                            )
                        }
                    }
                }
                "Letters" -> {
                    val letters = ('A'..'Z').map { it.toString() }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(letters) { letter ->
                            FilterCard(
                                filter = Filter(name = letter),
                                filterName = filter.name,
                                filterIndex = 0,
                                isSelected = selectedFilterItem?.name == letter,
                                onClick = {
                                    selectedFilterItem = Filter(name = letter)
                                    viewModel.getMealsByLetter(letter)
                                }
                            )
                        }
                    }
                }
            }
        }
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .weight(1f),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(filters) {
//                FilterCard(
//                    filter = it,
//                    isSelected = selectedFilter == it,
//                    onClick = {
//                        selectedFilter = it
//                        onFilterSelected(it)
//                    }
//                )
//            }
//        }
        IconButton(onClick = {
            isFilterExpanded = !isFilterExpanded
        },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Surface(
                modifier = Modifier.diagonalGradientBorder(
                    colors = listOf(Caramel40, Caramel40),
                    shape = CircleShape
                ),
                color = MaterialTheme.colorScheme.background
            ) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
                    contentDescription = "Filters",
                    modifier = Modifier.background(Color.Transparent),
                )
            }
//        if (isFilterExpanded) {
            DropdownMenu(
                expanded = isFilterExpanded,
                onDismissRequest = { isFilterExpanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                filters.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Text(text = filter.name, color = Orange80)
                        },
                        onClick = {
                        selectedFilter = filter
                        onFilterSelected(filter)
                        isFilterExpanded = false // Close dropdown on selection
                    })
                }
            }
        }
//        }
    }
}

@Composable
fun FilterCard(modifier: Modifier = Modifier, filter: Filter, filterName: String, filterIndex: Int, isSelected: Boolean, onClick: () -> Unit){
    val border = Modifier.fadeInGradientBorder(
        showBorder = !isSelected,
        colors = listOf(Caramel40, Orange80),
        shape = RoundedCornerShape(10.dp)
    )

    Card(
        modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() }
            .then(border),
        shape = RoundedCornerShape(10.dp),
        colors = if (isSelected) CardDefaults.cardColors(Orange80) else CardDefaults.cardColors(
            MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = modifier
            .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            when(filterName){
                "Category" -> {
                    AsyncImage(
                        model = filter.icon,
                        contentDescription = "filter.strCategory",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                    Text(modifier = Modifier
                        .padding(start = 5.dp),
                        text = filter.name,
                        style = MaterialTheme.typography.bodySmall)
                }
                "Area" -> {
//                    if (filterIndex < flags.size) {
                        Image(
                            painter = painterResource(id = flags.get(filterIndex)),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                        )
//                    }
                    Text(modifier = Modifier
                        .padding(start = 5.dp),
                        text = filter.name,
                        style = MaterialTheme.typography.bodySmall)
                }
                "Letters" -> {
                    Text(modifier = Modifier
                        .padding(5.dp),
                        text = filter.name,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

val flags = listOf(
    R.drawable.american,
    R.drawable.british,
    R.drawable.canadian,
    R.drawable.chinese,
    R.drawable.croatian,
    R.drawable.dutch,
    R.drawable.egyptian,
    R.drawable.filipino,
    R.drawable.french,
    R.drawable.greek,
    R.drawable.indian,
    R.drawable.irish,
    R.drawable.italian,
    R.drawable.jamaican,
    R.drawable.japanese,
    R.drawable.kenyan,
    R.drawable.malaysian,
    R.drawable.mexican,
    R.drawable.moroccan,
    R.drawable.polish,
    R.drawable.portuguese,
    R.drawable.russian,
    R.drawable.spanish,
    R.drawable.thai,
    R.drawable.tunisian,
    R.drawable.turkish,
    R.drawable.ukrainian,
    R.drawable.unknown,
    R.drawable.vietnamese,
)