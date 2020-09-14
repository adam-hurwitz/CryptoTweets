package app.cryptotweets.feed.viewmodel

import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet
import io.reactivex.rxjava3.subjects.BehaviorSubject

data class _FeedViewState(
    val _feed: BehaviorSubject<PagedList<Tweet>> = BehaviorSubject.create()
)

data class FeedViewState(private val _viewState: _FeedViewState) {
    val feed: BehaviorSubject<PagedList<Tweet>> = _viewState._feed
}