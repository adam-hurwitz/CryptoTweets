package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cryptotweets.Utils.Status.*
import app.cryptotweets.feed.FeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository
) : ViewModel() {
    val LOG = FeedViewModel::class.java.simpleName

    init {
        viewModelScope.launch(Dispatchers.IO) {
            feedRepository.getTweets().collect { results ->
                when (results.status) {
                    LOADING -> Log.v(LOG, "Loading") //TODO: Show progressBar.
                    SUCCESS -> withContext(Dispatchers.Main) {
                        //TODO: Populate view state.
                        results.data?.map {
                            Log.v(LOG, "Tweet: " + it)
                        }
                    }
                    ERROR -> Log.e(LOG, "Error") //TODO: Show snackbar.
                }
            }
        }
    }
}