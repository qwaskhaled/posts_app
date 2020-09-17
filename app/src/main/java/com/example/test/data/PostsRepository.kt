package com.example.test.data

import PostBoundaryCallback
import androidx.paging.LivePagedListBuilder
import com.example.test.data.local.PostDatabase
import com.example.test.data.remote.PostService
import com.example.test.model.Post
import com.example.test.model.PostsListResult
import com.example.test.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PostsRepository @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val service: PostService,
    private val cache: PostDatabase
) {

    fun getPosts(): PostsListResult {
        val dataSourceFactory = cache.postsDao().getPosts()
        val boundaryCallback = PostBoundaryCallback(service, cache)
        val networkErrors = boundaryCallback.networkErrors
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return PostsListResult(data, networkErrors)
    }

    suspend fun createPost(post: Post): Result<Unit> = withContext(ioDispatcher) {
        try {
            val postResult = service.insertPost(title = post.name, thumbnailUrl = post.thumbnail)
            sortPostAndCache(postResult)

            return@withContext Result.Success(Unit)

        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    suspend fun updatePost(post: Post): Result<Unit> = withContext(ioDispatcher) {
        try {
            service.updatePost(
                id = post.id.toInt(),
                title = post.name,
                thumbnailUrl = post.thumbnail
            )

            cache.postsDao().updatePost(post = post.copy(id = post.id))
            return@withContext Result.Success(Unit)

        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    suspend fun deletePost(post: Post): Result<Unit> = withContext(ioDispatcher) {
        try {
            service.deletePost(id = post.id.toInt())
            cache.postsDao().deletePostById(postId = post.roomId)
            return@withContext Result.Success(Unit)

        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    private fun sortPostAndCache(post: Post) {
        val postsList = cache.postsDao().getPostsList()
        val newPostsList = mutableListOf<Post>()

        newPostsList.add(post.copy(roomId = 0))
        postsList.forEachIndexed { index, oldPost ->
            newPostsList.add(oldPost.copy(roomId = index + 1.toLong()))
        }

        cache.postsDao().insertAll(newPostsList)
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}