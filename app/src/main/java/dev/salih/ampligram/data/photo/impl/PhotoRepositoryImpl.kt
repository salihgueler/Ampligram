package dev.salih.ampligram.data.photo.impl

import com.amplifyframework.api.aws.GsonVariablesSerializer
import com.amplifyframework.api.graphql.SimpleGraphQLRequest
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.kotlin.core.Amplify
import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.model.Comment
import dev.salih.ampligram.model.Photo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import java.io.InputStream
import java.util.*
import kotlin.collections.first
import com.amplifyframework.datastore.generated.model.Comment as RemoteComment
import com.amplifyframework.datastore.generated.model.Photo as RemotePhoto

class PhotoRepositoryImpl : PhotoRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPhotos(): Result<List<Photo>> {
        return runCatching {
            val remotePhotos = Amplify.DataStore.query(RemotePhoto::class).toList()

            val photos = remotePhotos.map { remotePhoto ->
                val url = Amplify.Storage.getUrl("${remotePhoto.photoKey}.png").url.toString()
                Photo(
                    id = remotePhoto.id,
                    description = remotePhoto.description,
                    username = remotePhoto.username,
                    url = url,
                    isFavorite = remotePhoto.isFavorite,
                    comments = remotePhoto.comments?.map { remoteComment ->
                        Comment(
                            id = remoteComment.id,
                            username = remoteComment.username,
                            comment = remoteComment.comment
                        )
                    } ?: emptyList(),
                    location = remotePhoto.location
                )
            }
            return Result.success(photos)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun toggleFavorite(id: String): Result<Unit> {
        return runCatching {
            val remotePhoto =
                Amplify.DataStore.query(
                    RemotePhoto::class,
                    Where.matches(RemotePhoto.ID.eq(id))
                ).toList().first()
            val updatedPhoto = remotePhoto
                .copyOfBuilder()
                .isFavorite(!remotePhoto.isFavorite)
                .build()
            Amplify.DataStore.save(updatedPhoto)
            return Result.success(Unit)
        }
    }

    override suspend fun getPhotoById(id: String): Result<Photo> {
        return runCatching {
            val document = ("query getPhoto(\$id: ID!) { "
                    + "getPhoto(id: \$id) { "
                    + "id "
                    + "photoKey "
                    + "description "
                    + "username "
                    + "location "
                    + "isFavorite "
                    + "comments { "
                    + "items { "
                    + "id "
                    + "username "
                    + "comment "
                    + "photoID "
                    + "} "
                    + "} "
                    + "} "
                    + "}")
            val request = SimpleGraphQLRequest<RemotePhoto>(
                document,
                mapOf("id" to id),
                RemotePhoto::class.java,
                GsonVariablesSerializer()
            )
            val remotePhoto = Amplify.API.query(request).data
            val url = Amplify.Storage.getUrl("${remotePhoto.photoKey}.png").url.toString()
            val photo = Photo(
                id = remotePhoto.id,
                description = remotePhoto.description,
                username = remotePhoto.username,
                url = url,
                isFavorite = remotePhoto.isFavorite,
                comments = remotePhoto.comments?.map { remoteComment ->
                    Comment(
                        id = remoteComment.id,
                        username = remoteComment.username,
                        comment = remoteComment.comment
                    )
                } ?: emptyList(),
                location = remotePhoto.location
            )
            return Result.success(photo)
        }
    }

    override suspend fun addComment(
        photoId: String,
        username: String,
        comment: String
    ): Result<Photo> {
        return runCatching {
            val remoteComment = RemoteComment.builder()
                .username(username)
                .comment(comment)
                .photoId(photoId)
                .build()
            Amplify.DataStore.save(remoteComment)
            val photo = getPhotoById(photoId).getOrThrow()
            return Result.success(photo)
        }
    }

    override suspend fun addPhoto(
        photoKey: String,
        description: String,
        username: String,
        location: String,
    ): Result<Boolean> {
        return runCatching {
            val remotePhoto = RemotePhoto.builder()
                .photoKey(photoKey)
                .description(description)
                .username(username)
                .location(location)
                .isFavorite(false)
                .build()
            Amplify.DataStore.save(remotePhoto)
            return Result.success(true)
        }
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