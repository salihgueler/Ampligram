package dev.salih.ampligram.ui.components.photo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PhotoThumbnail(
    imageUrl: String,
    contentDescription: String,
    onImageClicked: () -> Unit
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onImageClicked)
            .padding(2.dp),
        contentScale = ContentScale.Crop
    )
}