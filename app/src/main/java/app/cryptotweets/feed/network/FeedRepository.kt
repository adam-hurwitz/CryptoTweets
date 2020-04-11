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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val dao: FeedDao,
    private val service: FeedService
) {
    fun initFeed(pagedListBoundaryCallBack: PagedList.BoundaryCallback<Tweet>) = flow {
        emit(loading(null))
        // Page number reset when new data request is made.
        sharedPreferences.edit().putInt(FEED_LIST_PAGE_NUM_KEY, FEED_LIST_PAGE_NUM_DEFAULT).apply()
        val page = sharedPreferences.getInt(FEED_LIST_PAGE_NUM_KEY, FEED_LIST_PAGE_NUM_DEFAULT)
        try {
            val tweetsResponse = getTweets(page)
            dao.addTweets(tweetsResponse)
            val tweetsQuery = dao.getAllTweets()
                .toLiveData(
                    pageSize = FEED_PAGEDLIST_SIZE,
                    boundaryCallback = pagedListBoundaryCallBack
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
            val tweetsResponse = getTweets(page)
            if (tweetsResponse.isNotEmpty()) {
                emit(success(null))
                dao.addTweets(tweetsResponse)
                sharedPreferences.edit().putInt(FEED_LIST_PAGE_NUM_KEY, page).apply()
            }
        } catch (exception: Exception) {
            emit(error(exception.localizedMessage!!, null))
        }
    }

    private suspend fun getTweets(page: Int): List<Tweet> {
        return service.getTweets(
            listType = FEED_LIST_TYPE,
            listId = FEED_LIST_ID,
            count = FEED_LIST_SIZE,
            page = page.toString()
        )
    }
}