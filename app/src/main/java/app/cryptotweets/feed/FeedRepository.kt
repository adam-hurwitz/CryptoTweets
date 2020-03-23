package app.cryptotweets.feed

import androidx.lifecycle.asFlow
import androidx.paging.toLiveData
import app.cryptotweets.Utils.FEED_LIST_ID
import app.cryptotweets.Utils.FEED_LIST_SIZE
import app.cryptotweets.Utils.FEED_LIST_TYPE
import app.cryptotweets.Utils.FEED_PAGE_SIZE
import app.cryptotweets.Utils.Resource.Companion.error
import app.cryptotweets.Utils.Resource.Companion.loading
import app.cryptotweets.Utils.Resource.Companion.success
import app.cryptotweets.feed.network.FeedBoundaryCallBack
import app.cryptotweets.feed.network.FeedService
import app.cryptotweets.feed.room.FeedDao
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val feedService: FeedService,
    private val feedDao: FeedDao
) {
    fun getFeed(feedRepoCallback: FeedRepoCallback) = flow {
        emit(loading(null))
        try {
            val tweetsResponse = feedService.getTweets(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_SIZE
            )
            feedDao.insertAll(tweetsResponse)
            feedDao.getAll().toLiveData(
                pageSize = FEED_PAGE_SIZE,
                //TODO: Experiment
                // Passing in CoroutineScope 'coroutineScope { this }'
                // emit to Loading in ViewModel, pass in FlowCollector 'this'
                boundaryCallback = FeedBoundaryCallBack(feedRepoCallback)
            ).asFlow().collect { results ->
                emit(success(results))
            }
        } catch (exception: Exception) {
            emit(error(exception.localizedMessage!!, null))
        }
    }

    fun getFeed2() = flow {
        emit(loading(null))
        try {
            val tweetsResponse = feedService.getTweets2(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_SIZE,
                //TODO: Manage current page with SharedPreferences
                page = "2"
            )
            feedDao.appendAll(tweetsResponse)
            println("BDC Repo")
        } catch (exception: Exception) {
            emit(error(exception.localizedMessage!!, null))
        }
    }
}

//TODO: Refactor
interface FeedRepoCallback {
    fun onItemEndLoaded()
}