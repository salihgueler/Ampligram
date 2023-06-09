package dev.salih.ampligram.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.salih.ampligram.data.photo.PhotoRepository
import dev.salih.ampligram.data.photo.impl.FakePhotoRepositoryImpl
import dev.salih.ampligram.data.user.UserRepository
import dev.salih.ampligram.data.user.impl.FakeUserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun bindPhotoRepository(): PhotoRepository {
        return FakePhotoRepositoryImpl()
    }

    @Singleton
    @Provides
    fun bindUserRepository(): UserRepository {
        return FakeUserRepositoryImpl()
    }
}