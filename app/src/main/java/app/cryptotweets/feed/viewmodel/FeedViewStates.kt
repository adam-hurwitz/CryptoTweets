package app.cryptotweets.feed.viewmodel

import androidx.paging.PagingData
import app.cryptotweets.feed.models.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

data class _FeedViewState(
    val _feed: MutableStateFlow<PagingData<Tweet>?> = MutableStateFlow(null)
)

data class FeedViewState(private val _viewState: _FeedViewState) {
    val feed: Flow<PagingData<Tweet>> = _viewState._feed.filterNotNull()
}