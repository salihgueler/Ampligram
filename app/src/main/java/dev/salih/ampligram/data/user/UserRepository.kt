package dev.salih.ampligram.data.user

import dev.salih.ampligram.model.User

interface UserRepository {
    fun getCurrentUser(): Result<User>
}