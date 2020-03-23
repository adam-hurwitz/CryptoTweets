package app.cryptotweets.feed.network

import androidx.paging.PagedList
import app.cryptotweets.feed.FeedRepoCallback
import app.cryptotweets.feed.models.Tweet

class FeedBoundaryCallBack(val feedRepoCallback: FeedRepoCallback) :
    PagedList.BoundaryCallback<Tweet>() {
    override fun onItemAtEndLoaded(itemAtEnd: Tweet) {
        super.onItemAtEndLoaded(itemAtEnd)
        println("BDC FeedBoundaryCallback ${itemAtEnd}")
        feedRepoCallback.onItemEndLoaded()
    }
}
