package app.cryptotweets.feed

import app.cryptotweets.Utils.FEED_EMPTY_OR_NULL_MESSAGE
import app.cryptotweets.Utils.FEED_LIST_COUNT
import app.cryptotweets.Utils.FEED_LIST_ID
import app.cryptotweets.Utils.FEED_LIST_TYPE
import app.cryptotweets.Utils.Resource.Companion.error
import app.cryptotweets.Utils.Resource.Companion.loading
import app.cryptotweets.Utils.Resource.Companion.success
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(private val feedService: FeedService) {

    suspend fun getTweets() = flow {
        emit(loading(null))
        try {
            val result = feedService.getTweets(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_COUNT
            )
            emit(success(result))
        } catch (exception: Exception) {
            emit(error(FEED_EMPTY_OR_NULL_MESSAGE, null))
        }
    }
}