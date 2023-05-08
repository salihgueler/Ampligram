package dev.salih.ampligram.ui.photo_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.data.user.UserRepository
import dev.salih.ampligram.model.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PhotoDetailUiState {
    data class Success(val photo: Photo) : PhotoDetailUiState()
    data class Error(val errorMessage: String) : PhotoDetailUiState()
    object Loading : PhotoDetailUiState()
}

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotoDetailUiState>(PhotoDetailUiState.Loading)
    val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()

    fun getPhotoById(id: String) {
        val result = photoRepository.getPhotoById(id)
        if (result.isSuccess) {
            _uiState.value = PhotoDetailUiState.Success(result.getOrThrow())
        } else {
            _uiState.value = PhotoDetailUiState.Error(
                result.exceptionOrNull()?.message
                    ?: "Something went wrong while retrieving photo. $result"
            )
        }
    }

    fun addComment(photoId: String, comment: String) {
        viewModelScope.launch {
            val username = userRepository.getCurrentUser()
            if (username.isSuccess) {
                val result = photoRepository.addComment(
                    photoId,
                    username.getOrThrow().username,
                    comment
                )
                if (result.isSuccess) {
                    _uiState.value = PhotoDetailUiState.Success(result.getOrThrow())
                } else {
                    _uiState.value = PhotoDetailUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Something went wrong while adding comment. $result"
                    )
                }
            } else {
                _uiState.value = PhotoDetailUiState.Error(
                    username.exceptionOrNull()?.message
                        ?: "Something went wrong while retrieving current user. $username"
                )
            }
        }
    }
}