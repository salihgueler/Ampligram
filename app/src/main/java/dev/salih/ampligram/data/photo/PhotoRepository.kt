package dev.salih.ampligram.data.photo

import dev.salih.ampligram.model.Photo
import java.io.InputStream

interface PhotoRepository {
    /// Returns the list of photos to be displayed in the home page.
    suspend fun getPhotos(): Result<List<Photo>>

    /// Toggles the favorite status of the photo with the given id.
    suspend fun toggleFavorite(id: String): Result<Unit>

    /// Returns the photo with the given id.
    suspend fun getPhotoById(id: String): Result<Photo>

    /// Adds a comment to the photo with the given id.
    suspend fun addComment(
        photoId: String,
        username: String,
        comment: String
    ): Result<Photo>

    /// Adds a photo to the list of photos.
    suspend fun addPhoto(
        photoKey: String,
        description: String,
        username: String,
        location: String,
    ): Result<Boolean>

    /// Uploads the photo to bucket
    suspend fun uploadImage(imageStream: InputStream): Result<String>
}