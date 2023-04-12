package dev.salih.ampligram.data.user.impl

import com.amplifyframework.kotlin.core.Amplify
import dev.salih.ampligram.data.user.UserRepository
import dev.salih.ampligram.model.User

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