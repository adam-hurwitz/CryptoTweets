package app.cryptotweets.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.network.FeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class FeedViewModel : ViewModel() {
    val LOG_TAG = FeedViewModel::class.java.simpleName

    private val repository = FeedRepository()

    val feed get() = _feed.filterNotNull()
    private val _feed: MutableStateFlow<PagingData<Tweet>> = MutableStateFlow(PagingData.empty())

    init {
        repository.initFeed().cachedIn(viewModelScope).onEach {
            _feed.value = it
        }.launchIn(viewModelScope)
    }
}