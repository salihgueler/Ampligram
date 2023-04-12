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

The Amplify Auth category provides an interface for authenticating a user. Behind the scenes, it provides the necessary authorization to the other Amplify categories. It comes with default, built-in support for Amazon Cognito User Pool and Identity Pool. The Amplify CLI helps you create and configure the auth category with an authentication provider.

## Adding Authentication to Project Setup

To add authentication to your project, run the following command:

```bash
amplify add auth
```

Like the other processes, authentication also will guide you through the process of adding authentication to your project. You will be asked to select the default configuration or select a social provider as well as configuring the app authentication manually. For this workshop you will be using the default.

```bash
Using service: Cognito, provided by: awscloudformation
 
 The current configured provider is Amazon Cognito. 
 
 Do you want to use the default authentication and security configuration? (Use arrow keys)
❯ Default configuration 
  Default configuration with Social Provider (Federation) 
  Manual configuration 
  I want to learn more. 
```

Next, selecct the type of "sign in" that you want to use. No matter what you select, you will be expected to have email entry from user for approving the account. For this workshop, you will be using *username*.

```bash
  How do you want users to be able to sign in? (Use arrow keys)
❯ Username 
  Email 
  Phone Number 
  Email or Phone Number 
  I want to learn more. 
```

When it asks you if you want to do anything else, you can say no and finalize the initialization of the authentication.

```bash
 Do you want to configure advanced settings? (Use arrow keys)
❯ No, I am done. 
  Yes, I want to make some additional changes.
```

Once you see the following message, you can see you added the authentication to your project successfully.

```bash
✅ Successfully added auth resource ampligramdab1c31f locally
```

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

## Adding Authentication to Project Libraries

There are two ways to use authentication libraries with your project. One of them is to use the libraries by binding each functionality to your own UI. The other one is to use the pre-built UI components that are provided by the Amplify library. In this workshop, you will be using the pre-built UI components. However, you can check the official docs [here](https://docs.amplify.aws/lib/auth/getting-started/q/platform/android/) for the details about the library usage.

To add the pre-built UI authentication libraries to your project, add the following dependencies to your `build.gradle` file and sync the libraries.

```groovy
dependencies {
    implementation 'com.amplifyframework:aws-auth-cognito:2.7.1'
    implementation 'com.amplifyframework.ui:authenticator:1.0.0-dev-preview.0'
}
```

Once the sync is done, go to the *AmpligramApp* class and update the code in **onCreate** method like the following:

```kotlin
try { 
  ++Amplify.addPlugin(AWSCognitoAuthPlugin())
    Amplify.configure(applicationContext)
    Log.i("Ampligram", "Initialized Amplify")
} catch (error: AmplifyException) {
    Log.e("Ampligram", "Could not initialize Amplify", error)
}
```

Once you updated the code, go to the *MainActivity* class and wrap the code in **NavHost** method as following:

```kotlin
Authenticator {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        ...
    }
}
```

Lastly, let's add a sign-out functionality and show how one can use the auth libraries. To do that, update the *AmpligramTopAppBar* at the *ProfileScreen* function:

```kotlin 
AmpligramTopAppBar(
    isBackButtonEnabled = isBackButtonEnabled,
    onBackButtonClick = onBackButtonClick,
    title = "Profile",
  ++actions = {
  ++    IconButton(onClick = onLogoutButtonClick) {
  ++        Icon(Icons.Default.ExitToApp, contentDescription = "Profile")
  ++    }
  ++}
)
```

And add the callback to the paramters of the function:

```kotlin
@Composable
fun ProfileScreen(
    isBackButtonEnabled: Boolean,
    onBackButtonClick: () -> Unit,
  ++onLogoutButtonClick: () -> Unit,
    onImageClick: (String) -> Unit,
    username: String,
    profilePictureUrl: String,
    photos: List<Photo>,
) {
    ...
}
```

Update the function call from the MainActivity class as well for the *ProfilePage*:

```kotlin
onLogoutButtonClick = {
    profileViewModel.logout()
}
```

Add a new function to the *ProfileViewModel* class called *logout*:

```kotlin
fun logout() {
    viewModelScope.launch {
        userRepository.logout()
    }
}
```

And update the *UserRepository* interface as the following:

```kotlin
interface UserRepository {
    suspend fun getCurrentUser(): Result<User>

    suspend fun logout(): Result<Boolean>
}
```

Lastly rename *FakeUserRepositoryImpl* to *UserRepositoryImpl* update the class to implement the new function using the Amplify library:

```kotlin
class UserRepositoryImpl : UserRepository {
    override suspend fun getCurrentUser(): Result<User> {
        return runCatching {
            val user = Amplify.Auth.getCurrentUser()
            return Result.success(
                User(
                    id = user.userId,
                    username = user.username,
                    profilePictureUrl = "https://pbs.twimg.com/profile_images/1636379155532222465/ppItDc5w_400x400.jpg"
                )
            )
        }
    }

    override suspend fun logout(): Result<Boolean> {
        return runCatching {
            Amplify.Auth.signOut()
            return Result.success(true)
        }
    }
}
```

And update every place that calls *getCurrentUser* function with the a call with viewModelScope

```kotlin
viewModelScope.launch {
    val currentUser = userRepository.getCurrentUser()
    ...
}
```

If you run the application now, you should see the following.

![Auth flow GIF](/static/authflow.gif)