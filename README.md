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

The Amplify Storage category provides an interface for managing user content for your app in public, protected, or private storage buckets. The Storage category comes with default built-in support for Amazon Simple Storage Service (S3). The Amplify CLI helps you to create and configure the storage buckets for your app. The Amplify AWS S3 Storage plugin leverages Amazon S3.

## Adding Storage to Project Setup

To add Storage to your project, run the following command in your project’s root folder:

```bash
amplify add storage
```

The CLI prompts you with the following questions:

```bash
? Select from one of the below mentioned services: (Use arrow keys)
❯ Content (Images, audio, video, etc.) 
  NoSQL Database 
```

Next it will ask you to provide a friendly name for your resource that will be used to label this category in the project (it will also give you a friendly name in case you can't come up with something):

```bash
? Provide a friendly name for your resource that will be used to label this category in the project: › s320a9f80a
```

Now it is time to provide a bucket name (it will also give you a friendly name in case you can't come up with something):

```bash
? Provide bucket name: › ampligramff006ab2bd654c0a867cd1159cedf0e5
```

Select only auth users to have access to the bucket for reading, deleting and writing:

```bash 
✔ Who should have access: · Auth users only
? What kind of access do you want for Authenticated users? …  (Use arrow keys or type to filter)
 ● create/update
 ● read
❯● delete
```

Select no to adding a lambda trigger and now you are set.

## Pushing the Changes to the Cloud
All of the changes that you have made to your project are local. To push the changes to the cloud, run the following command:

```bash
amplify push
```

> Alternatively you can also run it with the `-y` flag to skip the confirmation prompt.

After a few minutes, if you see the following message, you can see that you have successfully pushed the changes to the cloud.

```bash
Deployment state saved successfully.
```

## Adding Storage to Project Libraries

To add storage libraries to your project, add the following dependencies to your `build.gradle` file and sync the libraries.

```groovy
dependencies {
    implementation 'com.amplifyframework:aws-storage-s3:2.7.1'
}
```

Once the sync is done, go to the *AmpligramApp* class and update the code in **onCreate** method like the following:

```kotlin
try { 
  ++Amplify.addPlugin(AWSS3StoragePlugin())
    Amplify.addPlugin(AWSCognitoAuthPlugin())
    Amplify.configure(applicationContext)
    Log.i("Ampligram", "Initialized Amplify")
} catch (error: AmplifyException) {
    Log.e("Ampligram", "Could not initialize Amplify", error)
}
```

## Uploading an image to S3

The photo picker mechanism is already implemented to the app. Now we need to upload the selected image to S3.

![Image Selection](/static/imageselection.png)

To upload the image to S3, we need to create a function that takes the image file and returns the key of the uploaded image. Update the functions at the *PhotoRepository* interface like the following:

```kotlin
/// Adds a photo to the list of photos.
fun addPhoto(photoKey: String, description: String, username: String): Result<Unit>

/// Uploads the photo to bucket
suspend fun uploadImage(imageStream: InputStream) : Result<String>
```

Then implement the uploadImage function in the *FakePhotoRepositoryImpl* class like the following:

> We will keep the name FakePhotoRepositoryImpl until we update all the functions in the following sections

```kotlin
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
override suspend fun uploadImage(imageStream: InputStream): Result<String> {
    return runCatching {
        val photoId = UUID.randomUUID().toString()
        Amplify.Storage.uploadInputStream("$photoId.png", imageStream).result()
        photoId
    }
}
```

This will get an input stream from the image file and upload it to the S3 bucket. For keeping a reference to the file, we are using the photoId as the key. The photoId is a unique identifier for the image file generated by the UUID.

For providing an input stream out of the selected uri, first go to the AddImageBottomSheetContent function and update the submitting mechanism like the following:

```kotlin
if (selectedImageUri != null && value.isNotBlank()) {
    val inputStream =
        context.contentResolver.openInputStream(selectedImageUri!!)
    value = ""
    if (inputStream != null) {
        onPhotoAdded(inputStream, value)
    }
    selectedImageUri = null
}
```

And get the current context like the following at the top of the function:

```kotlin
val context = LocalContext.current
```

Lastly, update each callback to return the input stream created.

If you go ahead and run our application, you can see that the image is uploaded to the S3 bucket. To see the uploaded files, go to the Amplify Studio and select the File Browser.

![File Browser](/static/filebrowser.gif)

## List all uploaded files

To list all the uploaded files, we need to update the *getPhotos* function in the *FakePhotoRepositoryImpl* class like the following:

```kotlin
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
```

This will get you the list of the image keys that is uploaded earlier on and generate Photo objects out ot them.

Be sure to update the function signature in the *PhotoRepository* interface as well.

```kotlin
suspend fun getPhotos(): Result<List<Photo>>
```

Lastly, call getPhotos function by using viewModelScope.launch in the each iteration:

```kotlin
viewModelScope.launch {
    _uiState.value = ProfileUiState.Loading
    val result = photoRepository.getPhotos()
    ...
}
```

![List Photos](/static/listofimages.png)