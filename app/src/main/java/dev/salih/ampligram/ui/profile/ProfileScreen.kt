package dev.salih.ampligram.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    isBackButtonEnabled: Boolean,
    onBackButtonClick: () -> Unit,
    onLogoutButtonClick: () -> Unit,
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
                title = "Profile",
                actions = {
                    IconButton(onClick = onLogoutButtonClick) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Profile")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
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
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
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