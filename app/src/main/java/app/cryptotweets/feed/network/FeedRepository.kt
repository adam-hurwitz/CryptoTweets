package app.cryptotweets.feed.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cryptotweets.utils.CryptoTweetsDatabase
import app.cryptotweets.utils.FEED_PAGEDLIST_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalPagingApi
class FeedRepository @Inject constructor(
    private val database: CryptoTweetsDatabase,
    private val service: FeedService
) {

    fun initFeed() = Pager(
        config = PagingConfig(pageSize = FEED_PAGEDLIST_SIZE),
        remoteMediator = FeedRemoteMediator(service, database),
        pagingSourceFactory = { database.feedDao().getAllTweets() }
    ).flow

}