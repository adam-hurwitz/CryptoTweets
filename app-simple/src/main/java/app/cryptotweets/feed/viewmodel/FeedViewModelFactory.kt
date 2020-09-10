package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import app.cryptotweets.feed.network.FeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class FeedViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val feedRepository: FeedRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = FeedViewModel(repository = feedRepository) as T
}