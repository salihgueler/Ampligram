package dev.salih.ampligram.ui.components.photo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.salih.ampligram.model.Photo

@Composable
fun PhotoCard(
    photo: Photo,
    onFavoriteClick: () -> Unit,
    onCommentClick: () -> Unit,
    onLocationClick: () -> Unit,
    onImageClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(300.dp)
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            AsyncImage(
                model = photo.url,
                contentDescription = photo.description,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onImageClick),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = photo.username,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                if (photo.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like"
                            )
                        }
                        IconButton(onClick = onCommentClick) {
                            Icon(Icons.Default.Send, contentDescription = "Comment")
                        }
                        IconButton(onClick = onLocationClick) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Open Map for Location"
                            )
                        }
                    }
                }
            }
        }
    }
}