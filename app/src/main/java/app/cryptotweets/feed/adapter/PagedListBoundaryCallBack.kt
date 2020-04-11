package app.cryptotweets.feed.adapter

import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.network.RepositoryLoadingCallback

class PagedListBoundaryCallBack(
    val repositoryLoadingCallback: RepositoryLoadingCallback,
    val toRetry: Boolean
) : PagedList.BoundaryCallback<Tweet>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        if (toRetry) repositoryLoadingCallback.onZeroItemsLoaded()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Tweet) {
        super.onItemAtEndLoaded(itemAtEnd)
        repositoryLoadingCallback.onItemEndLoaded()
    }
}