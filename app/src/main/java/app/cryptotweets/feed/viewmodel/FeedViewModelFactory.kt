package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.savedstate.SavedStateRegistryOwner
import app.cryptotweets.feed.network.FeedRepository

@ExperimentalPagingApi
class FeedViewModelFactory constructor(
    owner: SavedStateRegistryOwner,
    private val feedRepository: FeedRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = FeedViewModel(feedRepository = feedRepository) as T
}