package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cryptotweets.Utils.Status.*
import app.cryptotweets.feed.FeedRepository
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

    private val _viewState = _FeedViewState()
    val viewState = FeedViewState(_viewState)

    private val _viewEffects = _FeedViewEffects()
    val viewEffects = FeedViewEffects(_viewEffects)

    init {
        initFeed(true)
    }

    private fun initFeed(toRetry: Boolean) {
        feedRepository.initFeed(this, toRetry).onEach { results ->
            withContext(Dispatchers.Main) {
                when (results.status) {
                    LOADING -> _viewEffects._isLoading.value = true
                    SUCCESS -> {
                        _viewEffects._isLoading.value = false
                        _viewState._feed.value = results.data
                    }
                    ERROR -> {
                        _viewEffects._isLoading.value = false
                        Log.e(LOG, "Error + ${results.message}")
                    } // TODO: Show snackbar.
                }
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