package app.cryptotweets.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import app.cryptotweets.feed.FeedRepository

class FeedViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository
) : ViewModel() {
    init {
        //TODO: Get Twitter feed.
    }
}