package dev.salih.ampligram.data.user.impl

import dev.salih.ampligram.data.user.UserRepository
import dev.salih.ampligram.model.User

class FakeUserRepositoryImpl : UserRepository {
    override fun getCurrentUser(): Result<User> {
        return Result.success(User("1", "salihgueler", "https://pbs.twimg.com/profile_images/1636379155532222465/ppItDc5w_400x400.jpg"))
    }
}