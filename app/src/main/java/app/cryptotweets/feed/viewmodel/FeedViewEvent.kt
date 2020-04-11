package app.cryptotweets.feed.viewmodel

interface FeedViewEvent {
    fun swipeToRefreshEvent()
    fun retryEvent()
}