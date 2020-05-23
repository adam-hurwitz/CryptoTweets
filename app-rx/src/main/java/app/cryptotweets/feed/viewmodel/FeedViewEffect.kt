package app.cryptotweets.feed.viewmodel

import app.cryptotweets.utils.Event
import io.reactivex.rxjava3.subjects.ReplaySubject

data class _FeedViewEffect(
    val _isLoading: ReplaySubject<Event<Boolean>> = ReplaySubject.create(),
    val _isError: ReplaySubject<Event<Boolean>> = ReplaySubject.create()
)

data class FeedViewEffect(private val _viewEffect: _FeedViewEffect) {
    val isLoading: ReplaySubject<Event<Boolean>> = _viewEffect._isLoading
    val isError: ReplaySubject<Event<Boolean>> = _viewEffect._isError
}