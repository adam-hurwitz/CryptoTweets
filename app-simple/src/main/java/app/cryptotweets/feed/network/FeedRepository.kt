package app.cryptotweets.feed.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cryptotweets.utils.FEED_PAGEDLIST_SIZE

class FeedRepository {
    fun initFeed() = Pager(
        config = PagingConfig(pageSize = FEED_PAGEDLIST_SIZE, enablePlaceholders = true),
        pagingSourceFactory = { FeedPagingSource() }
    ).flow
}