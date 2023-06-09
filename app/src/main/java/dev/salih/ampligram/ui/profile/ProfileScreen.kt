package dev.salih.ampligram.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.salih.ampligram.model.Photo
import dev.salih.ampligram.ui.components.common.AmpligramTopAppBar
import dev.salih.ampligram.ui.components.photo.PhotoThumbnail

@Composable
fun ProfileScreen(
    isBackButtonEnabled: Boolean,
    onBackButtonClick: () -> Unit,
    onImageClick: (String) -> Unit,
    username: String,
    profilePictureUrl: String,
    photos: List<Photo>,
) {
    Scaffold(
        topBar = {
            AmpligramTopAppBar(
                isBackButtonEnabled = isBackButtonEnabled,
                onBackButtonClick = onBackButtonClick,
                title = "Profile"
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = profilePictureUrl,
                contentDescription = "Photo of $username",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = username,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
            ) {
                items(photos) { photo ->
                    PhotoThumbnail(
                        imageUrl = photo.url,
                        contentDescription = photo.url,
                        onImageClicked = { onImageClick(photo.id)}
                    )
                }
            }
        }
    }
}