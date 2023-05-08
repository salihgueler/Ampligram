package dev.salih.ampligram.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.data.photo.impl.PhotoRepositoryImpl
import dev.salih.ampligram.data.user.UserRepository
import dev.salih.ampligram.data.user.impl.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun bindPhotoRepository(): PhotoRepository {
        return PhotoRepositoryImpl()
    }

    @Singleton
    @Provides
    fun bindUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }
}