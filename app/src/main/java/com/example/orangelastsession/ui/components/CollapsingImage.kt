package com.example.orangelastsession.ui.components

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import com.example.orangelastsession.ui.screens.mealsDeatails.CollapsedImageSize
import com.example.orangelastsession.ui.screens.mealsDeatails.ExpandedImageSize
import com.example.orangelastsession.ui.screens.mealsDeatails.Hzpadding
import com.example.orangelastsession.ui.screens.mealsDeatails.MaxTitleOffset
import com.example.orangelastsession.ui.screens.mealsDeatails.MinImageOffset
import com.example.orangelastsession.ui.screens.mealsDeatails.MinTitleOffset
import kotlin.math.max
import kotlin.math.min

@Composable
fun MealImageDetails(
    imageUrl: String,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }
    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = Hzpadding.then(Modifier.statusBarsPadding())
    ) {
        MealImage(imageUrl = imageUrl, contentDescription = null, surfaceColor = MaterialTheme.colorScheme.background)
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurable, constraints ->
        check(measurable.size == 1)
        val collapseFraction = collapseFractionProvider()
        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurable[0].measure(Constraints.fixed(imageWidth, imageWidth))
        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction)
            .roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }


}
