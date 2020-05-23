package app.cryptotweets.feed.viewmodel

import app.cryptotweets.utils.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

@ExperimentalCoroutinesApi
data class _FeedViewEffect(
        val _isLoading: MutableStateFlow<Event<Boolean?>> = MutableStateFlow(Event(null)),
        val _isError: MutableStateFlow<Event<Boolean?>> = MutableStateFlow(Event(null))
)

@ExperimentalCoroutinesApi
data class FeedViewEffect(private val _viewEffect: _FeedViewEffect) {
    val isLoading: Flow<Event<Boolean?>> = _viewEffect._isLoading.filterNotNull()
    val isError: Flow<Event<Boolean?>> = _viewEffect._isError.filterNotNull()
}