package dev.salih.ampligram.data.photo.impl

import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.model.Comment
import dev.salih.ampligram.model.Photo

private val photos = mutableListOf<Photo>()

class FakePhotoRepositoryImpl : PhotoRepository {
    override fun getPhotos(): Result<List<Photo>> {
        val list = mutableListOf<Photo>()
        photos.forEach {
            list.add(it.copy())
        }
        return Result.success(list)
    }

    override fun toggleFavorite(id: String): Result<Unit> {
        val photo = photos.find { it.id == id }
        photo?.let {
            it.isFavorite = !it.isFavorite
        }
        return Result.success(Unit)
    }

    override fun getPhotoById(id: String): Result<Photo> {
        val photo = photos.find { it.id == id }
        return if (photo != null) {
            Result.success(photo.copy())
        } else {
            Result.failure(Exception("Photo not found"))
        }
    }

    override fun addComment(photoId: String, username: String, comment: String): Result<Photo> {
        val photo = photos.find { it.id == photoId }
        photo?.let {
            val newComment = Comment(
                id = "${it.comments.size + 1}",
                username = username,
                comment = comment
            )
            it.comments = it.comments.toMutableList().apply { add(newComment) }
        }
        return if (photo != null) {
            Result.success(photo.copy())
        } else {
            Result.failure(Exception("Photo not found"))
        }
    }

    override fun addPhoto(
        photoUri: String,
        description: String,
        username: String
    ): Result<Unit> {
        val id = if (photos.isEmpty()) {
            "1"
        } else {
            "${photos.last().id.toInt() + 1}"
        }
        val photo = Photo(
            id = id,
            username = username,
            url = photoUri,
            description = description,
            isFavorite = false,
            comments = emptyList(),
            location = "Selected Location"
        )
        photos.add(photo)
        return Result.success(Unit)
    }


}