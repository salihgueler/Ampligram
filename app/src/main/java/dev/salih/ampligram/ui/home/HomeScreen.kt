package dev.salih.ampligram.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.salih.ampligram.model.Photo
import dev.salih.ampligram.ui.add_image.AddImageBottomSheetContent
import dev.salih.ampligram.ui.components.common.AmpligramTopAppBar
import dev.salih.ampligram.ui.components.common.CenteredCircularProgressIndicator
import dev.salih.ampligram.ui.components.photo.PhotoCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onProfileButtonClicked: () -> Unit,
    onFavoriteClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onImageClick: (String) -> Unit,
    onLocationClick: (String) -> Unit,
    onPhotoAdded: (String, String) -> Unit,
    photos: List<Photo>,
    isLoading: Boolean,
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetContent = {
            AddImageBottomSheetContent(
                onPhotoAdded = { photoUri, description ->
                    onPhotoAdded(photoUri, description)
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                shouldShowKeyboard = modalBottomSheetState.isVisible
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Scaffold(
            topBar = {
                AmpligramTopAppBar(
                    actions = {
                        IconButton(onClick = onProfileButtonClicked) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Photo")
                }
            },
        ) {
            Surface(
                modifier = Modifier.padding(it)
            ) {
                if (isLoading) {
                    CenteredCircularProgressIndicator()
                } else if (photos.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Create,
                                contentDescription = "Photo addition Icon",
                                modifier = Modifier
                                    .padding(bottom = 8.dp, top = 8.dp)
                                    .size(32.dp)
                            )
                            Text(
                                text = "There is no photo.\nClick the + button to add a photo",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        items(photos) { photo ->
                            PhotoCard(
                                photo = photo,
                                onFavoriteClick = {
                                    onFavoriteClick(photo.id)
                                },
                                onCommentClick = {
                                    onCommentClick(photo.id)
                                },
                                onLocationClick = {
                                    onLocationClick(photo.location)
                                },
                                onImageClick = {
                                    onImageClick(photo.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}