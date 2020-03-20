package app.cryptotweets.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet

data class _FeedViewState(
    val _feed: MutableLiveData<PagedList<Tweet>> = MutableLiveData()
)

data class FeedViewState(private val _feedViewState: _FeedViewState) {
    val feed: LiveData<PagedList<Tweet>> = _feedViewState._feed
}