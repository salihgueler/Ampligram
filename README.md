In this workshop, you will learn how you can develop a photo sharing application by using AWS Amplify and Jetpack Compose with the following features:

- Authentication
- Create and query data
- Upload and retrieve images
- In-app map application

*GIF of the app will come here*

[Jetpack Compose](https://developer.android.com/jetpack/compose) is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android. Quickly bring your app to life with less code, powerful tools, and intuitive Kotlin APIs.

[AWS Amplify](https://aws.amazon.com/amplify) is a complete solution that lets frontend web and mobile developers easily build, ship, and host full-stack applications on AWS, with the flexibility to leverage the breadth of AWS services as use cases evolve. No cloud expertise needed.

**Requirements**

- AWS Account
- Android Studio version 4.0 or higher
- Android SDK API level 24 (Android 7.0) or higher

Amplify DataStore provides a programming model for leveraging shared and distributed data without writing additional code for offline and online scenarios, which makes working with distributed, cross-user data just as simple as working with local-only data.

> DataStore allows you to start persisting data locally to your device with DataStore, even without an AWS account.

## Adding DataStore to Project Setup

To add DataStore to your project, you need to enable the GraphQL API category for the  

```bash
amplify add api
```

When prompted, select the GraphQL option:

```bash
? Select from one of the below mentioned services: (Use arrow keys)
❯ GraphQL
REST 
```

For enabling DataStore, you need to enable the conflict detection:

```bash
? Here is the GraphQL API that we will create. Select a setting to edit or continue 
  Name: ampligram 
  Authorization modes: API key (default, expiration time: 7 days from now) 
❯ Conflict detection (required for DataStore): Disabled 
  Continue 
```

Select the default resolution option as Auto Merge:

```bash
? Select the default resolution strategy (Use arrow keys)
❯ Auto Merge 
  Optimistic Concurrency 
  Custom Lambda 
  Learn More 
```

Now update the authorization mode to use Amazon Cognito User Pool:

```bash
? Choose the default authorization type for the API 
  API key 
❯ Amazon Cognito User Pool 
  IAM 
  OpenID Connect 
  Lambda 
```

Next, select the *Continue* option and select the **Single object with fields** schema template:

```bash
? Choose a schema template: (Use arrow keys)
❯ Single object with fields (e.g., “Todo” with ID, name, description) 
  One-to-many relationship (e.g., “Blogs” with “Posts” and “Comments”) 
  Objects with fine-grained access control (e.g., a project management app with owner-based authorization) 
  Blank Schema 
```

Now, you need to update schema.graphql file like the following:

```graphql
type Photo @model @auth(rules: [{ allow: owner }]) {
  id: ID!
  photoKey: String!
  description: String!
  username: String!
  location: String!
  comments: [String!]!
  isFavorite: Boolean!
}
```

Now, you need to push the changes to the cloud:

```bash
amplify push
```

Once the changes are pushed, you can see that the deployment has happened for the GraphQL API and an endpoint URL is provided to us:

```bash
✅ GraphQL schema compiled successfully.

GraphQL endpoint: https://7s7xhllkkzhk3fyvo3xir2rmda.appsync-api.eu-central-1.amazonaws.com/graphql

GraphQL transformer version: 2
```

One important point to mention here is that out of the provided GraphQL schema, it generated the Photo object in your project. The advantage of this is to provide you an abstraction over the data operations by giving you a chance to use an actual object.

> The generated objects are Java classes but you can still use them thanks to Kotlin’s interoperability.

## Adding DataStore to Project Libraries

Now, you need to add the following dependencies to your project:

```groovy
dependencies {
    implementation 'com.amplifyframework:aws-datastore:2.7.1'
}
```
```groovy
try {
    ++ Amplify.addPlugin(AWSDataStorePlugin())
    Amplify.addPlugin(AWSS3StoragePlugin())
    Amplify.addPlugin(AWSCognitoAuthPlugin())
    Amplify.configure(applicationContext)
    Log.i("Ampligram", "Initialized Amplify")
} catch (error: AmplifyException) {
    Log.e("Ampligram", "Could not initialize Amplify", error)
}
```

## Saving a Photo object with DataStore

Before you implement the code for saving the photo object, first update the *FakePhotoRepositoryImpl* to *PhotoRepositoryImpl*, because at the end of this step, you should have real data served by the GraphQL API.

First let's update the *PhotoRepository* interface like the following:

```kotlin
interface PhotoRepository {
    /// Returns the list of photos to be displayed in the home page.
    suspend fun getPhotos(): Result<List<Photo>>

    /// Toggles the favorite status of the photo with the given id.
    suspend fun toggleFavorite(id: String): Result<Unit>

    /// Returns the photo with the given id.
    suspend fun getPhotoById(id: String): Result<Photo>

    /// Adds a comment to the photo with the given id.
    fun addComment(photoId: String, username: String, comment: String): Result<Photo>

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
```

Now, you need to update the *PhotoRepositoryImpl* class with **suspend** keywords.

After you update them, you can update the *addPhoto*:

```kotlin
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
            .comments(emptyList())
            .isFavorite(false)
            .build()
        Amplify.DataStore.save(remotePhoto)
        return Result.success(true)
    }
}
```

You might be wondering why you have a **RemoteObject** when you did not have one before. The reason is that for preventing the confusion with the local object, you will import the generated photo object as RemotePhoto and it will be only used with the repository.

```kotlin
import com.amplifyframework.datastore.generated.model.Photo as RemotePhoto
```

## Retrieving Photo objects with DataStore

Now, you need to update the *getPhotos* method in the *PhotoRepositoryImpl* class:

```kotlin
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
                comments = emptyList(),
                location = remotePhoto.location
            )
        }
        return Result.success(photos)
    }
}
```

With this change, you only query photos from the DataStore and you do not need to query them from local instance anymore. 

You will keep a reference to the photos until you clean up the rest of the functions. 

## Query a single Photo object by ID

Amplify Libraries support predicates to be able to get the item with a pre-condition. 

Update the *getPhotoById* method in the *PhotoRepositoryImpl* class as the following:

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
override suspend fun getPhotoById(id: String): Result<Photo> {
    return runCatching {
        val remotePhoto =
            Amplify.DataStore.query(
                RemotePhoto::class,
                Where.matches(RemotePhoto.ID.eq(id))
            ).first()
        val url = Amplify.Storage.getUrl("${remotePhoto.photoKey}.png").url.toString()
        val photo = Photo(
            id = remotePhoto.id,
            description = remotePhoto.description,
            username = remotePhoto.username,
            url = url,
            isFavorite = remotePhoto.isFavorite,
            comments = emptyList(),
            location = remotePhoto.location
        )
        return Result.success(photo)
    }
}
```

With the implementation above, you have queried the photos that matches an id. Since you know that each photo has a unique id, you can use the first item of the list.

## Updating an item with DataStore

For updating a certain item, you need to use the copying mechanism of the generated model.

```kotlin
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
```

The *.copyOfBuilder()* method creates a new builder with the same values as the current object. Then, you can update the values that you want to change and call the *.build()* method to create a new object with the updated values.

Once you run the application now, you should be able to see that items are reacting to the favoriting action and persist it:

![Uploaded items](/static/uploadeditems.png)

## Creating a list of Comments inside the Photos with DataStore

Each post has a comment when you open up the detail page. Start by updating the *schema.graphql* file:

```graphql
type Comment @model @auth(rules: [{allow: private}]) {
  id: ID!
  username: String!
  comment: String!
  photoID: ID! @index(name: "byPhoto")
}

type Photo @model @auth(rules: [{allow: private}]) {
  id: ID!
  photoKey: String!
  description: String!
  username: String!
  location: String!
  isFavorite: Boolean!
  comments: [Comment] @hasMany(indexName: "byPhoto", fields: ["id"])
}
```

The *Comment* type has a *photoID* field that is an index to the *Photo* type. Let's push these changes to the backend by calling `amplify push`.

Next thing to do is to update the *addComment* function signature in *PhotoRepository* interface:

```kotlin
/// Adds a comment to the photo with the given id.
suspend fun addComment(
    photoId: String, 
    username: String, 
    comment: String
): Result<Photo>
```

Afterwards, go to the *PhotoRepositoryImpl* class and update the *addComment* method implementation:

```kotlin
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
```

The implementation above does the following: 

You create a new comment object and save it to the DataStore. Then, you query the photo with the given id and return it.

## Running custom GraphQL queries
When you want to reach out to the information requiring nested data, there are two ways to reach out to that data, you can either run a query for each object that you want to reach out or you need to run a custom GraphQL query. 

The first approach is not efficient since you need to run a query for each object that you want to reach out. The reason that you need to do a call for getting nested data is that the querying objects does not work in a recursive fashion to bring all related data.

Update the *getPhotoById* method in the *PhotoRepositoryImpl* class as the following to get the photo with the comments with a single call:

```kotlin
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
```



