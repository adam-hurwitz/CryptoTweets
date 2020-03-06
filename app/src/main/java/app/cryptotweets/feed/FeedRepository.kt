package app.cryptotweets.feed

import app.cryptotweets.Utils.FEED_LIST_COUNT
import app.cryptotweets.Utils.FEED_LIST_ID
import app.cryptotweets.Utils.FEED_LIST_TYPE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(private val feedService: FeedService) {

    suspend fun getTweets() = feedService.getTweets(
        listType = FEED_LIST_TYPE,
        listId = FEED_LIST_ID,
        count = FEED_LIST_COUNT
    )

}