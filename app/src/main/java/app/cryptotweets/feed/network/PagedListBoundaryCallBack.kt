package app.cryptotweets.feed.network

import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet

class PagedListBoundaryCallBack(val repoLoadMoreCallback: RepoLoadMoreCallback) :
    PagedList.BoundaryCallback<Tweet>() {
    override fun onItemAtEndLoaded(itemAtEnd: Tweet) {
        super.onItemAtEndLoaded(itemAtEnd)
        repoLoadMoreCallback.onItemEndLoaded()
    }
}