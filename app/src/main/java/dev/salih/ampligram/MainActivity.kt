package dev.salih.ampligram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.salih.ampligram.ui.home.HomeScreen
import dev.salih.ampligram.ui.home.HomeUiState
import dev.salih.ampligram.ui.home.HomeViewModel
import dev.salih.ampligram.ui.photo_detail.PhotoDetailScreen
import dev.salih.ampligram.ui.photo_detail.PhotoDetailUiState
import dev.salih.ampligram.ui.photo_detail.PhotoDetailViewModel
import dev.salih.ampligram.ui.profile.ProfileScreen
import dev.salih.ampligram.ui.profile.ProfileUiState
import dev.salih.ampligram.ui.profile.ProfileViewModel
import dev.salih.ampligram.ui.theme.AmpligramTheme

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmpligramTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            val homeViewModel: HomeViewModel = hiltViewModel()
                            val state = homeViewModel.uiState.collectAsState().value
                            val photos = (state as? HomeUiState.Success)?.photos ?: emptyList()

                            HomeScreen(
                                onProfileButtonClicked = {
                                    navController.navigate("profile")
                                },
                                onFavoriteClick = { id ->
                                    homeViewModel.toggleFavorite(id)
                                },
                                onCommentClick = {
                                    navController.navigate("photo-detail/${it}/shouldShowCommentEntry=true")
                                },
                                onImageClick = {
                                    navController.navigate("photo-detail/${it}/shouldShowCommentEntry=false")
                                },
                                onLocationClick = { },
                                onPhotoAdded = { photoUrl, description ->
                                    homeViewModel.addPhoto(photoUrl, description)
                                },
                                isLoading = state is HomeUiState.Loading,
                                photos = photos
                            )
                        }
                        composable("profile") {
                            val profileViewModel: ProfileViewModel = hiltViewModel()
                            val state = profileViewModel.uiState.collectAsState().value
                            val photos = (state as? ProfileUiState.Success)?.photos ?: emptyList()
                            val user = (state as? ProfileUiState.Success)?.currentUser
                            ProfileScreen(
                                isBackButtonEnabled = navController.previousBackStackEntry != null,
                                onBackButtonClick = {
                                    navController.popBackStack()
                                },
                                username = user?.username ?: "Couldn't retrieve username",
                                profilePictureUrl = user?.profilePictureUrl
                                    ?: "Couldn't retrieve profile picture",
                                photos = photos,
                                onImageClick = {
                                    navController.navigate("photo-detail/${it}/shouldShowCommentEntry=false")
                                },
                            )
                        }
                        composable(
                            "photo-detail/{photoId}/shouldShowCommentEntry={shouldShowCommentEntry}",
                            arguments = listOf(
                                navArgument("shouldShowCommentEntry") {
                                    type = NavType.BoolType
                                },
                            )
                        ) {
                            val profileViewModel: PhotoDetailViewModel = hiltViewModel()
                            val photoId = it.arguments?.getString("photoId")
                            val shouldShowCommentEntry =
                                it.arguments?.getBoolean("shouldShowCommentEntry") ?: false
                            if (photoId != null) {
                                profileViewModel.getPhotoById(photoId)
                            }
                            val state = profileViewModel.uiState.collectAsState().value
                            val photo = (state as? PhotoDetailUiState.Success)?.photo
                            PhotoDetailScreen(
                                isBackButtonEnabled = navController.previousBackStackEntry != null,
                                onBackButtonClick = {
                                    navController.popBackStack()
                                },
                                photoUrl = photo?.url ?: "Photo url couldn't be retrieved",
                                description = photo?.description
                                    ?: "Description couldn't be retrieved",
                                username = photo?.username ?: "Username couldn't be retrieved",
                                shouldOpenCommentEntry = shouldShowCommentEntry,
                                comments = photo?.comments ?: emptyList(),
                                isLoading = state is PhotoDetailUiState.Loading,
                                onCommentSubmitted = { comment ->
                                    profileViewModel.addComment(photoId!!, comment)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}