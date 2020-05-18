package app.cryptotweets.feed.viewmodel

import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
data class _FeedViewState(
    val _feed: MutableStateFlow<PagedList<Tweet>?> = MutableStateFlow(null)
)

@ExperimentalCoroutinesApi
data class FeedViewState(private val _viewState: _FeedViewState) {
    val feed: StateFlow<PagedList<Tweet>?> = _viewState._feed
}