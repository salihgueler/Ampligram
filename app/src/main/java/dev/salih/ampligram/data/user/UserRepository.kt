package dev.salih.ampligram.data.user

import dev.salih.ampligram.model.User

interface UserRepository {
    suspend fun getCurrentUser(): Result<User>

    suspend fun logout(): Result<Boolean>
}