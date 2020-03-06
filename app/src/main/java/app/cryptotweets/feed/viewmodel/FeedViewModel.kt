package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cryptotweets.feed.FeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val results = feedRepository.getTweets()
            withContext(Dispatchers.Main) {
                //TODO: Populate view state.
                results.map {
                    Log.v(FeedViewModel::class.java.simpleName, "Tweet: " + it)
                }
            }
        }
    }
}