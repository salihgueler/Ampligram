package dev.salih.ampligram.data.photo.impl

import com.amplifyframework.kotlin.core.Amplify
import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.model.Comment
import dev.salih.ampligram.model.Photo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.io.InputStream
import java.util.*

private val photos = mutableListOf<Photo>()

class FakePhotoRepositoryImpl : PhotoRepository {
    override suspend fun getPhotos(): Result<List<Photo>> {
        return runCatching {
            val imageKeys = Amplify.Storage.list("")
            val urls = imageKeys.items.map { Amplify.Storage.getUrl(it.key).url }
            urls.forEachIndexed { index, url ->
                val photo = Photo(
                    id = "${index + 1}",
                    description = "Description ${index + 1}",
                    username = "Username ${index + 1}",
                    url = url.toString(),
                    isFavorite = false,
                    comments = emptyList(),
                    location = "Berlin, Germany"
                )
                photos.add(photo)
            }
            return Result.success(photos)
        }
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
        photoKey: String,
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
            url = photoKey,
            description = description,
            isFavorite = false,
            comments = emptyList(),
            location = "Selected Location"
        )
        photos.add(photo)
        return Result.success(Unit)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override suspend fun uploadImage(imageStream: InputStream): Result<String> {
        return runCatching {
            val photoId = UUID.randomUUID().toString()
            Amplify.Storage.uploadInputStream("$photoId.png", imageStream).result()
            photoId
        }
    }
}