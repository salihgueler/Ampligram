package dev.salih.ampligram

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AmpligramApp: Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("Ampligram", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("Ampligram", "Could not initialize Amplify", error)
        }
    }
}