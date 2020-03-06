package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cryptotweets.feed.FeedRepository
import kotlinx.coroutines.launch

class FeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository
) : ViewModel() {
    init {
        //TODO: Launching Coroutine on IO thread, calling suspend function, and returning data on Main thread
        viewModelScope.launch {
            val results = feedRepository.getTweets()
            Log.v("ViewModel", "Results: " + results)
        }
    }
}