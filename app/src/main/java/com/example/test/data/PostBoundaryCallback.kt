import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.test.data.local.PostDatabase
import com.example.test.data.remote.PostService
import com.example.test.model.Post
import com.example.test.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This boundary callback gets notified when user reaches to the edges of the list for example when
 * the database cannot provide any more data.
 **/
class PostBoundaryCallback(
    private val service: PostService,
    private val cache: PostDatabase
) : PagedList.BoundaryCallback<Post>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    private val _networkErrors = MutableLiveData<Event<String>>()
    val networkErrors: LiveData<Event<String>>
        get() = _networkErrors

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData(0)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Post) {
        requestAndSaveData(itemAtEnd.id.toInt())
    }

    private fun requestAndSaveData(id: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        CoroutineScope(Dispatchers.IO).launch {
            isRequestInProgress = try {
                val result = service.getPosts(id, NETWORK_PAGE_SIZE)
                result.map { post -> post.roomId = post.id }
                cache.postsDao().insertAll(result)
                false
            } catch (e: Exception) {
                _networkErrors.postValue(Event(e.message.orEmpty()))
                false
            }
        }
    }
}