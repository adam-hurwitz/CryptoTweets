package app.cryptotweets.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

class FeedViewModelFactory(owner: SavedStateRegistryOwner) : AbstractSavedStateViewModelFactory(
    owner,
    null
) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = FeedViewModel(savedStateHandle = handle) as T

}