package app.cryptotweets.feed.network

import android.util.Log
import androidx.paging.PagingSource
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.utils.FEED_LIST_ID
import app.cryptotweets.utils.FEED_LIST_SIZE
import app.cryptotweets.utils.FEED_LIST_TYPE
import app.cryptotweets.utils.Injection

class FeedPagingSource : PagingSource<Int, Tweet>() {
    private val LOG_TAG = FeedPagingSource::class.java.simpleName

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tweet> {
        try {
            val page = params.key ?: 1
            val nextPage = page + 1
            val tweets = Injection.feedService.getTweets(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_SIZE,
                page = page.toString()
            )
            Log.v(LOG_TAG, "load success: ${tweets}")
            return LoadResult.Page(
                data = tweets,
                prevKey = null, // Only paging forward.
                nextKey = nextPage
            )
        } catch (error: Exception) {
            Log.e(LOG_TAG, "load error: ${error.localizedMessage}")
            return LoadResult.Error(error)
        }
    }
}

