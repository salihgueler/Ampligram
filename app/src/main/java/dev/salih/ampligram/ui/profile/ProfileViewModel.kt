package dev.salih.ampligram.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.data.user.UserRepository
import dev.salih.ampligram.model.Photo
import dev.salih.ampligram.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class ProfileUiState {
    data class Success(val photos: List<Photo>, val currentUser: User) : ProfileUiState()
    data class Error(val errorMessage: String) : ProfileUiState()
    object Loading : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

        private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
        val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

        init {
            getProfileInformation()
        }

        private fun getProfileInformation() {
            _uiState.value = ProfileUiState.Loading
            val result = photoRepository.getPhotos()
            if (result.isSuccess) {
                val currentUser = userRepository.getCurrentUser()
                if (currentUser.isSuccess) {
                    _uiState.value = ProfileUiState.Success(result.getOrThrow(), currentUser.getOrThrow())
                } else {
                    _uiState.value = ProfileUiState.Error(currentUser.exceptionOrNull()?.message ?: "Something went wrong while retrieving user information. $currentUser")
                }
            } else {
                _uiState.value = ProfileUiState.Error(result.exceptionOrNull()?.message ?: "Something went wrong while retrieving photos. $result")
            }
        }
}