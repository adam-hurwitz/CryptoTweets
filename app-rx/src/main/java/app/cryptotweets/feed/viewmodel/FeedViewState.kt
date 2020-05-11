package app.cryptotweets.feed.viewmodel

import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet
import io.reactivex.rxjava3.subjects.ReplaySubject

data class _FeedViewState(
    val _feed: ReplaySubject<PagedList<Tweet>> = ReplaySubject.create()
)

data class FeedViewState(private val _viewState: _FeedViewState) {
    val feed: ReplaySubject<PagedList<Tweet>> = _viewState._feed
}