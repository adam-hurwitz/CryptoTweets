package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cryptotweets.Utils.Status.*
import app.cryptotweets.feed.FeedRepository
import app.cryptotweets.feed.FeedViewState
import app.cryptotweets.feed._FeedViewState
import app.cryptotweets.feed.network.RepositoryLoadingCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class FeedViewModel(
    private val feedRepository: FeedRepository
) : ViewModel(), RepositoryLoadingCallback {
    val LOG = FeedViewModel::class.java.simpleName

    private val _feedViewState = _FeedViewState()
    val feedViewState = FeedViewState(_feedViewState)

    init {
        initFeed(true)
    }

    private fun initFeed(toRetry: Boolean) {
        feedRepository.initFeed(this, toRetry).onEach { results ->
            when (results.status) {
                LOADING -> {
                    // TODO: Show progressBar.
                    Log.v(LOG, "Loading")
                }
                SUCCESS -> withContext(Dispatchers.Main) {
                    _feedViewState._feed.value = results.data
                }
                ERROR -> Log.e(LOG, "Error + ${results.message}") // TODO: Show snackbar.
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    override fun onZeroItemsLoaded() {
        initFeed(false)
    }

    override fun onItemEndLoaded() {
        feedRepository.loadMoreFeed().onEach { results ->
            when (results.status) {
                LOADING -> println("Loading") // TODO: Show progressBar.
                SUCCESS -> println("Success") // TODO: Log.
                ERROR -> println("Error") // TODO: Show snackbar.
            }
        }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }
}