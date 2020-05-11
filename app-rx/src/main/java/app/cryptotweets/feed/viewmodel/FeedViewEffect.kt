package app.cryptotweets.feed.viewmodel

import io.reactivex.rxjava3.subjects.ReplaySubject

data class _FeedViewEffect(
    val _isLoading: ReplaySubject<Boolean> = ReplaySubject.create(),
    val _isError: ReplaySubject<Boolean> = ReplaySubject.create()
)

data class FeedViewEffect(private val _viewEffect: _FeedViewEffect) {
    val isLoading: ReplaySubject<Boolean> = _viewEffect._isLoading
    val isError: ReplaySubject<Boolean> = _viewEffect._isError
}