package app.cryptotweets.feed

import android.content.SharedPreferences
import androidx.lifecycle.asFlow
import androidx.paging.toLiveData
import app.cryptotweets.Utils.*
import app.cryptotweets.Utils.Resource.Companion.error
import app.cryptotweets.Utils.Resource.Companion.loading
import app.cryptotweets.Utils.Resource.Companion.success
import app.cryptotweets.feed.network.FeedService
import app.cryptotweets.feed.network.PagedListBoundaryCallBack
import app.cryptotweets.feed.network.RepoLoadMoreCallback
import app.cryptotweets.feed.room.FeedDao
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
    fun initFeed(repoLoadMoreCallback: RepoLoadMoreCallback) = flow {
        emit(loading(null))
        try {
            sharedPreferences.edit()
                .putInt(FEED_LIST_PAGE_NUM_KEY, FEED_LIST_PAGE_NUM_DEFAULT).apply()
            val tweetsResponse = service.getTweets(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_SIZE,
                page = sharedPreferences
                    .getInt(FEED_LIST_PAGE_NUM_KEY, FEED_LIST_PAGE_NUM_DEFAULT).toString()
            )
            dao.insertAll(tweetsResponse)
            dao.getAll().toLiveData(
                pageSize = FEED_PAGEDLIST_SIZE,
                // TODO: Experiment
                //  Passing in CoroutineScope 'coroutineScope { this }'
                //  emit to Loading in ViewModel, pass in FlowCollector 'this'
                boundaryCallback = PagedListBoundaryCallBack(repoLoadMoreCallback)
            ).asFlow().collect { results ->
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
        sharedPreferences.edit().putInt(FEED_LIST_PAGE_NUM_KEY, page).apply()
        try {
            val tweetsResponse = service.getTweets(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_SIZE,
                page = page.toString()
            )
            if (tweetsResponse.isNotEmpty()) {
                emit(success(null))
                dao.insertAll(tweetsResponse)
            }
        } catch (exception: Exception) {
            emit(error(exception.localizedMessage!!, null))
        }
    }
}