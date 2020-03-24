package app.cryptotweets.feed.network

import androidx.paging.PagedList
import app.cryptotweets.feed.models.Tweet

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