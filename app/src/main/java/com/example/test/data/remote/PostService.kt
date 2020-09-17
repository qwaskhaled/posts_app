package com.example.test.data.remote

import com.example.test.model.Post
import com.example.test.model.Result
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface PostService {

    @GET("photos")
    suspend fun getPosts(
        @Query("_start") page: Int, @Query("_limit") itemsPerPage: Int
    ): List<Post>

    @FormUrlEncoded
    @POST("photos")
    suspend fun insertPost(
        @Field("title") title: String,
        @Field("thumbnailUrl") thumbnailUrl: String
    ): Post

    @FormUrlEncoded
    @PUT("photos/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("thumbnailUrl") thumbnailUrl: String
    ):Post

    @DELETE("photos/{id}")
    suspend fun deletePost(@Path("id") id: Int)

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create(): PostService {
            val client = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PostService::class.java)
        }
    }
}