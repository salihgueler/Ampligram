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

# What is AWS Amplify?

AWS Amplify is the set of tools that is aimed to help web and mobile developers to develop full stack applications.

![AWS Amplify Category](/static/awsamplify.jpg)

With AWS Amplify, you can build your backend visually for all apps, build and host your web applications and use AWS technologies.

You can use AWS Amplify with:
- [Amplify Libraries](#amplify_libraries) are powered by the AWS cloud and offer a pluggable model which can be extended to use other providers.
- [Amplify CLI](#amplify_cli) is a unified toolchain to create, integrate, and manage the AWS cloud services for your app.
- [Amplify Studio](#amplify_studio) is a visual development environment for building fullstack web and mobile apps.

## Amplify Libraries

The Amplify open-source client libraries provide use-case centric, opinionated, declarative, and easy-to-use interfaces across different categories of cloud powered operations enabling mobile and web developers to easily interact with their backends.

These libraries are powered by the AWS cloud and offer a pluggable model which can be extended to use other providers. The libraries can be used with both new backends created using the Amplify CLI and existing backend resources.

**For Android AWS Amplify supports the following categories:**

![Amplify Libraries](amplify_libraries)

- **Analytics**
    - The Analytics category enables you to collect analytics data for your App. The Analytics category comes with built-in support for Amazon Pinpoint and Amazon Kinesis (Kinesis support is currently only available in the Amplify JavaScript library).
- **GraphQL API**
    - The GraphQL API category provides an interface for retrieving and persisting your model data. The API category comes with default built-in support for AWS AppSync. The Amplify CLI allows you to define your API and provision a GraphQL service with CRUD operations and real-time functionality.
- **REST API**
    - The REST API category provides an interface for making requests to your backend. The Amplify CLI deploys REST APIs and handlers using Amazon API Gateway and AWS Lambda.
- **Authentication**
    - The Amplify Auth category provides an interface for authenticating a user. Behind the scenes, it provides the necessary authorization to the other Amplify categories. It comes with default, built-in support for Amazon Cognito User Pool and Identity Pool. The Amplify CLI helps you create and configure the auth category with an authentication provider.
- **DataStore**
    - Amplify DataStore provides a programming model for leveraging shared and distributed data without writing additional code for offline and online scenarios, which makes working with distributed, cross-user data just as simple as working with local-only data.
- **Geo**
    - Amplify Geo provides APIs and map UI components for mobile app development such that you can add maps to your app in just a few lines of code. Amplify Geo APIs are powered by Amazon Location Service and the map UI components from MapLibre are already integrated with the Geo APIs. You can quickly get started using Amplify CLI to provision your map resources.
- **Predictions**
    - The Predictions category enables you to integrate machine learning into your application without any prior machine learning experience. It supports translating text from one language to another, converting text to speech, text recognition from an image, entities recognition, labeling real world objects, interpretation of text, and uploading images for automatic training. This functionality is powered by AWS services including: Amazon Translate, Amazon Polly, Amazon Transcribe, Amazon Rekognition, Amazon Textract, and Amazon Comprehend.
- **Push notifications**
    - The Push Notifications category allows you to integrate push notifications in your app with Amazon Pinpoint targeting, campaign, and journey management support. You can segment your users, trigger push notifications to your app, and record metrics in Pinpoint when users receive or open notifications. Amazon Pinpoint helps you to create messaging campaigns and journeys targeted to specific user segments or demographics and collect interaction metrics with push notifications.
- **Storage**
  - The Storage category enables you to store and retrieve files from the cloud. The Amplify CLI helps you to configure the Storage category with Amazon S3, Amazon DynamoDB, and Amazon Simple Storage Service (Amazon S3) as the backend storage provider.

## [Amplify CLI](amplify_cli)

The Amplify Command Line Interface (CLI) is a unified toolchain to create, integrate, and manage the AWS cloud services for your app.

![Amplify CLI features](/static/amplifycli.gif)

You can use Amplify CLI for:
- Data modeling with GraphQL
- Managing multiple environments
- Manage extensibility
    - Override generated resources
    - Add custom AWS resources
    - Import existing AWS resources
    - Command hooks
    - Export Amplify project to CDK

### Installing Amplify CLI

**Requirements**

- Install Node.js® and NPM if they are not already on your machine.
- Verify that you are running at least Node.js version 12.x and npm version 6.x or greater by running node -v and npm -v in a terminal/console window
- Create AWS Account. If you don't already have an AWS account, you'll need to create one in order to follow the steps outlined in this tutorial.

**NPM**
```bash
npm install -g @aws-amplify/cli
```

**cURL (macOS and Linux)**
```bash
curl -sL https://aws-amplify.github.io/amplify-cli/install | bash && $SHELL
```

**cURL (Windows)**
```bash
curl -sL https://aws-amplify.github.io/amplify-cli/install-win -o install.cmd && install.cmd
```

### Configuring Amplify CLI

Run the following command to start the configuration process:

```bash
amplify configure
```

This command will ask you to sign into the AWS Console. Afterwards, select a region to create the project. After that, navigate to the [IAM User creation page](https://console.aws.amazon.com/iamv2/home#/users/create) if it's not already open.

> Keep your terminal open until you come back to it at at later stage from where you left of.

![User creation page](https://docs.amplify.aws/images/cli/user-creation/user-name.png)

Add a user name and click on *Next*.

![Policies page](https://docs.amplify.aws/images/cli/user-creation/user-permissions.png)

Select **Attach policies directly** option and search for *AdministratorAccess-Amplify* and select it and navigate to review page.

![Review page](https://docs.amplify.aws/images/cli/user-creation/user-review.png)

Click on the *Create User* after reviewing the page.

![User list page](https://docs.amplify.aws/images/cli/user-creation/user-list.png)

Now click on the created user, on the list.

![User details page](https://docs.amplify.aws/images/cli/user-creation/create-access-keys.png)

First click on the *Security credentials* tab and click on the *Create access key* button at the bottom.

![Access key page](https://docs.amplify.aws/images/cli/user-creation/ack-page.png)

Select *Command Line Interface*, check the checkbox for the warning, and select *Next* and on the next page select *Create access key*.

![Retrieve access key](https://docs.amplify.aws/images/cli/user-creation/access-keys-done.png)

Copy these values and paste them into the terminal and give a profile name.

```bash
:::code{language=bash showLineNumbers=false}
Specify the AWS Region
? region:  # Your preferred region
Follow the instructions at
https://docs.amplify.aws/cli/start/install/#configure-the-amplify-cli

to complete the user creation in the AWS console
https://console.aws.amazon.com/iamv2/home#/users/create

Press Enter to continue

Enter the access key of the newly created user:
? accessKeyId:  # YOUR_ACCESS_KEY_ID
? secretAccessKey:  # YOUR_SECRET_ACCESS_KEY
This would update/create the AWS Profile in your local machine
? Profile Name:  # (default)

Successfully set up the new user.
```

Now you are ready to use Amplify CLI.

## [Amplify Studio](amplify_studio) 

Amplify Studio is a visual development environment for building fullstack web and mobile apps.

![Amplify Studio](/static/amplifystudio.png)

## Key features of Amplify Studio

### Create and observe your data visually
If you want to design your data layer for your app, you can do it visually with Amplify Studio. You can create your data models, and then observe the data in real-time for your GraphQL API.

![Amplify Studio Data](/static/studiodata.png)

### Create Web Applications with Form Builder
You can also create React applications with Amplify Studio. You can create forms and then generate React code for your application.

![Amplify Studio Form Builder](/static/amplifyform.png)

### Generate React Code with Figma to Code integration
You can also generate React code from your Figma designs. You can use the Figma to Code integration to generate React code from your Figma designs.

![Amplify Studio Figma to Code](/static/figmatocode.png)

>Amplify Studio's UI features such as creating data and ui component builders are free. For pulling the data to your local machine, you will need to have Amplify CLI configured.

## Initializing Amplify Project

For starting off, you need to clone the starter project. You can clone the starter project from [here](https://github.com/salihgueler/Ampligram/tree/starter_project). After cloning the project, you need to initialize the project. Before initializing be sure the terminal is in the project directory.

```bash
cd wherever/project/is/at/Ampligram
```

## Initializing Amplify Project

To initialize the project, you need to run the following command:

```bash
amplify init
```

First select a project name, you can also accept the suggested name by using the name in the parenthesis by clicking *Enter*:

```bash
Note: It is recommended to run this command from the root of your app directory
? Enter a name for the project (Ampligram) 
``` 

Then you can either select the default configuration or you can change it by writing *n* later on:

```bash
The following configuration will be applied:

Project information
| Name: Ampligram
| Environment: dev
| Default editor: Visual Studio Code
| App type: android
| Res directory: app/src/main/res

? Initialize the project with the above configuration? (Y/n) 
```

Select the default configuration by clicking *Enter*.

```bash
? Select the authentication method you want to use: (Use arrow keys)
❯ AWS profile 
```

Select the *AWS profile* from the list and select the profile that you created before:

```bash
? Please choose the profile you want to use (default) 
```

Then wait for it to initialize the project. After the initialization is done, you can see the following message:

```bash
Deployment state saved successfully.
✔ Initialized provider successfully.
✅ Initialized your environment successfully.
```

Now it is time to initialize the Amplify Libraries to the project.

```groovy
android {
    compileOptions {
        // Support for Java 8 features
        coreLibraryDesugaringEnabled true
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Amplify core dependency
    implementation 'com.amplifyframework:core:2.5.0'

    // Support for Java 8 features
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.1.5'
}
```

Add the above code to the `build.gradle` file in the `app` directory. After adding the code, you need to sync the project.

Lastly, update the `AmplifyApp` as the following:

```kotlin
import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.core.Amplify
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AmpligramApp: Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            Amplify.configure(applicationContext)
            Log.i("Ampligram", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("Ampligram", "Could not initialize Amplify", error)
        }
    }
}
```

When you run the app, if you see the following log, it means that you have successfully initialized the project:

```bash
2023-11-03 14:00:00.000 12345-12345/dev.salih.ampligram I/Ampligram: Initialized Amplify
```