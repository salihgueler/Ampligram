package dev.salih.ampligram.ui.home

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
import java.io.InputStream
import javax.inject.Inject

sealed class HomeUiState {
    data class Success(val photos: List<Photo>) : HomeUiState()
    data class Error(val errorMessage: String) : HomeUiState()
    object Loading : HomeUiState()
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = photoRepository.getPhotos()
            if (result.isSuccess) {
                _uiState.value = HomeUiState.Success(result.getOrThrow())
            } else {
                _uiState.value = HomeUiState.Error(
                    result.exceptionOrNull()?.message ?: "Something went wrong. $result"
                )
            }
        }
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            val result = photoRepository.toggleFavorite(id)
            if (result.isSuccess) {
                getPhotos()
            } else {
                _uiState.value = HomeUiState.Error(
                    result.exceptionOrNull()?.message ?: "Something went wrong. $result"
                )
            }
        }
    }

    fun addPhoto(photoStream: InputStream, description: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                val usernameResult = userRepository.getCurrentUser()
                val photoKey = photoRepository.uploadImage(photoStream).getOrThrow()
                if (usernameResult.isSuccess) {
                    val result = photoRepository.addPhoto(
                        photoKey,
                        description,
                        usernameResult.getOrThrow().username,
                        "Berlin, Germany",
                    )
                    if (result.isSuccess) {
                        getPhotos()
                    } else {
                        _uiState.value = HomeUiState.Error(
                            result.exceptionOrNull()?.message ?: "Something went wrong. $result"
                        )
                    }
                } else {
                    _uiState.value = HomeUiState.Error(
                        usernameResult.exceptionOrNull()?.message
                            ?: "Something went wrong. $usernameResult"
                    )
                }
            }
        }
    }
}