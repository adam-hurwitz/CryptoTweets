package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import app.cryptotweets.feed.network.FeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class FeedViewModel(private val feedRepository: FeedRepository) : ViewModel() {
    val LOG_TAG = FeedViewModel::class.java.simpleName

    val viewState get() = FeedViewState(_viewState)
    private val _viewState = _FeedViewState()

    init {
        initFeed()
    }

    private fun initFeed() {
        feedRepository.initFeed().cachedIn(viewModelScope).onEach { results ->
            _viewState._feed.value = results
        }.launchIn(viewModelScope)
    }
}