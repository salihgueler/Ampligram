package dev.salih.ampligram.data.photo

import dev.salih.ampligram.model.Photo

interface PhotoRepository {
    /// Returns the list of photos to be displayed in the home page.
    fun getPhotos(): Result<List<Photo>>

    /// Toggles the favorite status of the photo with the given id.
    fun toggleFavorite(id: String): Result<Unit>

    /// Returns the photo with the given id.
    fun getPhotoById(id: String): Result<Photo>

    /// Adds a comment to the photo with the given id.
    fun addComment(photoId: String, username: String, comment: String): Result<Photo>

    /// Adds a photo to the list of photos.
    fun addPhoto(photoUri: String, description: String, username: String): Result<Unit>
}