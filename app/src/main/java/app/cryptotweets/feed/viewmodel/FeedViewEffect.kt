package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class _FeedViewEffect(
    val _isLoading: MutableLiveData<Boolean> = MutableLiveData(),
    val _isError: MutableLiveData<Boolean> = MutableLiveData()
)

data class FeedViewEffect(private val _viewEffect: _FeedViewEffect) {
    val isLoading: LiveData<Boolean> = _viewEffect._isLoading
    val isError: LiveData<Boolean> = _viewEffect._isError
}