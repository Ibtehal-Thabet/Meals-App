package com.example.orangelastsession.ui.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.orangelastsession.ui.theme.Caramel40
import com.example.orangelastsession.ui.theme.Caramel80
import com.example.orangelastsession.ui.theme.Orange40
import com.example.orangelastsession.ui.theme.Orange80

@Composable
fun GradientAuthBox(
    modifier: Modifier = Modifier, 
    content: @Composable BoxScope.()-> Unit
){
    Box(modifier = modifier.background(brush = Brush.linearGradient(
        listOf(
            Orange80,
            Caramel80
        )
    ))){
        content()
    }
}