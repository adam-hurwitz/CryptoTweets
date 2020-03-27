package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet

data class _FeedViewState(
    val _feed: MutableLiveData<PagedList<Tweet>> = MutableLiveData()
)

data class FeedViewState(private val _viewState: _FeedViewState) {
    val feed: LiveData<PagedList<Tweet>> = _viewState._feed
}