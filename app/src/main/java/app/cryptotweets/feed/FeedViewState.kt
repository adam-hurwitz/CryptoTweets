package app.cryptotweets.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.cryptotweets.feed.models.Tweet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

data class _FeedViewState(
    val _feed: MutableLiveData<List<Tweet>> = MutableLiveData()
)

data class FeedViewState(private val _feedViewState: _FeedViewState) {
    val feed: LiveData<List<Tweet>> = _feedViewState._feed
}