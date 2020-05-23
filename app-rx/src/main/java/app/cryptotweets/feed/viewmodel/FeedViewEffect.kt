package app.cryptotweets.feed.viewmodel

import app.cryptotweets.utils.Event
import io.reactivex.rxjava3.subjects.BehaviorSubject

data class _FeedViewEffect(
        val _isLoading: BehaviorSubject<Event<Boolean>> = BehaviorSubject.create(),
        val _isError: BehaviorSubject<Event<Boolean>> = BehaviorSubject.create()
)

data class FeedViewEffect(private val _viewEffect: _FeedViewEffect) {
    val isLoading: BehaviorSubject<Event<Boolean>> = _viewEffect._isLoading
    val isError: BehaviorSubject<Event<Boolean>> = _viewEffect._isError
}