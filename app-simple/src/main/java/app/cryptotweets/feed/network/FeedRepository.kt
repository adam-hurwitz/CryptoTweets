package app.cryptotweets.feed.network

import android.content.SharedPreferences
import androidx.lifecycle.asFlow
import androidx.paging.PagedList
import androidx.paging.toLiveData
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.room.FeedDao
import app.cryptotweets.utils.FEED_LIST_ID
import app.cryptotweets.utils.FEED_LIST_PAGE_NUM_DEFAULT
import app.cryptotweets.utils.FEED_LIST_PAGE_NUM_KEY
import app.cryptotweets.utils.FEED_LIST_SIZE
import app.cryptotweets.utils.FEED_LIST_TYPE
import app.cryptotweets.utils.FEED_PAGEDLIST_SIZE
import app.cryptotweets.utils.Resource.Companion.error
import app.cryptotweets.utils.Resource.Companion.loading
import app.cryptotweets.utils.Resource.Companion.success
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FeedRepository(
    private val sharedPreferences: SharedPreferences,
    private val dao: FeedDao,
    private val api: FeedApi
) {

    fun initFeed(boundaryCallBack: PagedList.BoundaryCallback<Tweet>) = flow {
        emit(loading(null))
        // Page number reset when new data request is made.
        val page = FEED_LIST_PAGE_NUM_DEFAULT
        try {
            dao.addTweets(getTweetsRequest(page))
            val tweetsQuery = dao.getAllTweets()
                .toLiveData(
                    pageSize = FEED_PAGEDLIST_SIZE,
                    boundaryCallback = boundaryCallBack
                ).asFlow()
            tweetsQuery.collect { results ->
                emit(success(results))
            }
        } catch (exception: Exception) {
            emit(error(exception.localizedMessage!!, null))
        }
    }

    fun loadMoreFeed() = flow {
        emit(loading(null))
        var page = sharedPreferences.getInt(FEED_LIST_PAGE_NUM_KEY, FEED_LIST_PAGE_NUM_DEFAULT)
        page++
        try {
            val tweetsResponse = getTweetsRequest(page)
            if (tweetsResponse.isNotEmpty()) {
                dao.addTweets(tweetsResponse)
                sharedPreferences.edit().putInt(FEED_LIST_PAGE_NUM_KEY, page).apply()
                emit(success(null))
            }
        } catch (exception: Exception) {
            emit(error(exception.localizedMessage!!, null))
        }
    }

    private suspend fun getTweetsRequest(page: Int) = api.getTweets(
        listType = FEED_LIST_TYPE,
        listId = FEED_LIST_ID,
        count = FEED_LIST_SIZE,
        page = page.toString()
    )

}