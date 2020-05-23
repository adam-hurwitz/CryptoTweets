package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import app.cryptotweets.feed.FeedFragment
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.utils.Event
import app.cryptotweets.utils.Status.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class FeedViewModel(private val feedRepository: FeedRepository) : ViewModel(), FeedViewEvent {
    val LOG_TAG = FeedViewModel::class.java.simpleName

    private val _viewState = _FeedViewState()
    val viewState = FeedViewState(_viewState)

    private val _viewEffect = _FeedViewEffect()
    val viewEffect = FeedViewEffect(_viewEffect)

    init {
        initFeed(true)
    }

    fun launchViewEvents(fragment: FeedFragment) {
        fragment.attachViewEvents(this)
    }

    override fun swipeToRefreshEvent() {
        initFeed(true)
    }

    override fun retryEvent() {
        initFeed(true)
    }

    private fun initFeed(toRetry: Boolean) {
        feedRepository.initFeed(pagedListBoundaryCallback(toRetry)).onEach { results ->
            when (results.status) {
                LOADING -> _viewEffect._isLoading.value = true
                SUCCESS -> {
                    _viewEffect._isLoading.value = false
                    _viewState._feed.value = results.data
                }
                ERROR -> {
                    _viewEffect._isLoading.value = false
                    _viewEffect._isError.value = Event(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun pagedListBoundaryCallback(toRetry: Boolean) =
            object : PagedList.BoundaryCallback<Tweet>() {

                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    if (toRetry) initFeed(false)
                }

                override fun onItemAtEndLoaded(itemAtEnd: Tweet) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    feedRepository.loadMoreFeed().onEach { results ->
                        when (results.status) {
                            LOADING -> Log.v(LOG_TAG, "onItemEndLoaded LOADING")
                            SUCCESS -> Log.v(LOG_TAG, "onItemEndLoaded SUCCESS")
                            ERROR -> _viewEffect._isError.value = Event(true)
                        }
                    }.launchIn(viewModelScope)
                }
            }
}