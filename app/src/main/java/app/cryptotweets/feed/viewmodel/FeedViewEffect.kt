package app.cryptotweets.feed.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@ExperimentalCoroutinesApi
data class _FeedViewEffect(
        val _isLoading: MutableStateFlow<Boolean?> = MutableStateFlow(null),
        val _isError: MutableStateFlow<Boolean?> = MutableStateFlow(null)
)

@ExperimentalCoroutinesApi
data class FeedViewEffect(private val _viewEffect: _FeedViewEffect) {
    val isLoading: Flow<Boolean> = _viewEffect._isLoading.filterNotNull().distinctUntilChanged()
    val isError: Flow<Boolean> = _viewEffect._isError.filterNotNull().distinctUntilChanged()
}