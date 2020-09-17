package com.example.test.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.test.util.Event

data class PostsListResult(
    val data: LiveData<PagedList<Post>>,
    val networkErrors: LiveData<Event<String>>
)