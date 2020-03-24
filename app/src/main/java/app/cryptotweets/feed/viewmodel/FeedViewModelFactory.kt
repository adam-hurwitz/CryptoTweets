package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import app.cryptotweets.feed.FeedRepository

class FeedViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val feedRepository: FeedRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = FeedViewModel(feedRepository = feedRepository) as T
}