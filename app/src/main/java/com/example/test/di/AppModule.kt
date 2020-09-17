package com.example.test.di

import android.content.Context
import com.example.test.data.PostsRepository
import com.example.test.data.local.PostDatabase
import com.example.test.data.remote.PostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO


    @Singleton
    @Provides
    fun providePostService(): PostService {
        return PostService.create()
    }

    @Singleton
    @Provides
    fun providePostDatabase(
        @ApplicationContext context: Context
    ): PostDatabase {
        return PostDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providePostsRepository(
        ioDispatcher: CoroutineDispatcher,
        postService: PostService,
        postDatabase: PostDatabase
    ): PostsRepository {
        return PostsRepository(ioDispatcher, postService, postDatabase)
    }
}

