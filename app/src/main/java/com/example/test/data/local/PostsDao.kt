package com.example.test.data.local

import androidx.paging.DataSource
import androidx.room.*
import com.example.test.model.Post

@Dao
interface PostsDao {

    @Query("SELECT * FROM Posts")
    fun getPosts(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM Posts")
    fun getPostsList(): List<Post>

    @Query("SELECT * FROM Posts ORDER BY id ASC LIMIT 1")
    fun getFirstPost(): Post

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post): Int

    @Query("DELETE FROM Posts WHERE roomId = :postId")
    suspend fun deletePostById(postId: Long): Int

    @Query("DELETE FROM Posts")
    suspend fun deletePosts()

}