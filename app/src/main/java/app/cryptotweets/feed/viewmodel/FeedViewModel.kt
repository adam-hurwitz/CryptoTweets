package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cryptotweets.Utils.Status.*
import app.cryptotweets.feed.FeedRepository
import app.cryptotweets.feed.FeedViewState
import app.cryptotweets.feed._FeedViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class FeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository
) : ViewModel() {
    val LOG = FeedViewModel::class.java.simpleName

    private val _feedViewState = _FeedViewState()
    val feedViewState = FeedViewState(_feedViewState)

    init {
        feedRepository.getFeed().onEach { results ->
                when (results.status) {
                    LOADING -> Log.v(LOG, "Loading") //TODO: Show progressBar.
                    SUCCESS -> withContext(Dispatchers.Main) {
                        _feedViewState._feed.value = results.data
                    }
                    ERROR -> Log.e(LOG, "Error + ${results.message}") //TODO: Show snackbar.
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}


