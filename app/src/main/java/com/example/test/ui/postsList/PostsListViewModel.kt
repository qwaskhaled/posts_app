package com.example.test.ui.postsList

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.test.R
import com.example.test.data.PostsRepository
import com.example.test.model.Post
import com.example.test.model.PostsListResult
import com.example.test.model.Result
import com.example.test.util.Event
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class PostsListViewModel @ViewModelInject constructor(
    private val postsRepository: PostsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _postsList = MutableLiveData<PostsListResult>()

    private val _toastEvent = MutableLiveData<Event<String>>()
    val toastEvent: LiveData<Event<String>> get() = _toastEvent

    private val _dataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> get() = _dataLoading

    val snackbarEvent: LiveData<Event<String>> = Transformations.switchMap(_postsList) {
        it.networkErrors
    }

    val posts: LiveData<PagedList<Post>> = Transformations.switchMap(_postsList) {
        it.data
    }

    init {
        getPosts()
    }

    fun getPosts() {
        _postsList.value = postsRepository.getPosts()
    }

    fun createPost(post: Post) = viewModelScope.launch {
        _dataLoading.value = Event(true)
        val postResult = postsRepository.createPost(post = post)

        _dataLoading.value = Event(false)
        if (postResult is Result.Success) {
            _toastEvent.value = Event(context.getString(R.string.success))
        } else {
            _toastEvent.value = Event(context.getString(R.string.error))
        }
    }

    fun editPost(post: Post) = viewModelScope.launch {
        _dataLoading.value = Event(true)
        val postResult = postsRepository.updatePost(post = post)

        _dataLoading.value = Event(false)
        if (postResult is Result.Success) {
            _toastEvent.value = Event(context.getString(R.string.success))
        } else {
            _toastEvent.value = Event(context.getString(R.string.error))
        }
    }

    fun deletePost(post: Post) = viewModelScope.launch {
        _dataLoading.value = Event(true)
        val postResult = postsRepository.deletePost(post = post)

        _dataLoading.value = Event(false)
        if (postResult is Result.Success) {
            _toastEvent.value = Event(context.getString(R.string.success))
        } else {
            _toastEvent.value = Event(context.getString(R.string.error))
        }
    }
}
