package app.cryptotweets.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import app.cryptotweets.R
import app.cryptotweets.feed.adapter.FeedCell
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.utils.TOP_TWEETS_FAVORITE_THRESHOLD
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
    private val _feed: MutableStateFlow<PagingData<FeedCell>> = MutableStateFlow(PagingData.empty())

    init {
        repository.initFeed().cachedIn(viewModelScope).onEach { pagingData ->
            _feed.value = pagingData.map {
                FeedCell.TweetCell(it)
            }.insertSeparators { before, after ->
                if (before == null) return@insertSeparators null // Beginning of the list
                else if (after == null) return@insertSeparators null // End of the list
                else {
                    val favorites = before.tweet.favorite_count
                    if (favorites > TOP_TWEETS_FAVORITE_THRESHOLD)
                        FeedCell.TopTweetCell(R.string.top_tweet_text, favorites)
                    else return@insertSeparators null
                }
            }
        }.launchIn(viewModelScope)
    }
}