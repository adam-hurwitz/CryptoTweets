package app.cryptotweets.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class _FeedViewEffects(
    val _isLoading: MutableLiveData<Boolean> = MutableLiveData(),
    val _isError: MutableLiveData<Boolean> = MutableLiveData()
)

data class FeedViewEffects(private val _viewEffects: _FeedViewEffects) {
    val isLoading: LiveData<Boolean> = _viewEffects._isLoading
    val isError: LiveData<Boolean> = _viewEffects._isError
}