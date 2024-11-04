package com.example.orangelastsession.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun YouTubeLink(youtubeUrl: String, logoPainter: Painter) {
    val context = LocalContext.current

    val annotatedText = AnnotatedString.Builder().apply {
        pushStringAnnotation(tag = "URL", annotation = youtubeUrl)
        withStyle(
            style = androidx.compose.ui.text.SpanStyle(
                color = Color(0xFF03A9F4),
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Watch full Recipe on YouTube")
        }
        pop()
    }.toAnnotatedString()

    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        Image(
            painter = logoPainter,
            contentDescription = "YouTube Logo",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            },
            style = TextStyle(fontSize = 18.sp)
        )
    }
}