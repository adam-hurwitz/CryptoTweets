package app.cryptotweets.feed.network

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.PagedList
import androidx.paging.toObservable
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.room.FeedDao
import app.cryptotweets.utils.FEED_LIST_ID
import app.cryptotweets.utils.FEED_LIST_PAGE_NUM_DEFAULT
import app.cryptotweets.utils.FEED_LIST_PAGE_NUM_KEY
import app.cryptotweets.utils.FEED_LIST_SIZE
import app.cryptotweets.utils.FEED_LIST_TYPE
import app.cryptotweets.utils.FEED_PAGEDLIST_SIZE
import app.cryptotweets.utils.Resource
import app.cryptotweets.utils.Resource.Companion.error
import app.cryptotweets.utils.Resource.Companion.loading
import app.cryptotweets.utils.Resource.Companion.success
import app.cryptotweets.utils.Status
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val dao: FeedDao,
    private val service: FeedService
) {
    val LOG_TAG = FeedRepository::class.java.simpleName

    fun initFeed(boundaryCallBack: PagedList.BoundaryCallback<Tweet>) =
        Observable.create<Resource<PagedList<Tweet>>> {
            val emit = it
            Log.v(LOG_TAG, "initFeed ${Status.LOADING.name}")
            emit.onNext(loading(null))
            // Page number reset when new data request is made.
            val page = FEED_LIST_PAGE_NUM_DEFAULT
            getTweetsRequest(page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError {
                    Log.e(LOG_TAG, "initFeed error: ${it.localizedMessage}")
                    emit.onNext(error(it.localizedMessage!!, null))
                }.subscribe {
                    Log.v(LOG_TAG, "initFeed: ${it}")
                    dao.addTweets(it)
                }
            val tweetsQuery = dao.getAllTweets().toObservable(
                pageSize = FEED_PAGEDLIST_SIZE,
                boundaryCallback = boundaryCallBack
            )
            tweetsQuery.subscribe { emit.onNext(success(it)) }
        }

    fun loadMoreFeed() = Observable.create<Resource<Nothing>> {
        val emit = it
        emit.onNext(loading(null))
        var page = sharedPreferences.getInt(FEED_LIST_PAGE_NUM_KEY, FEED_LIST_PAGE_NUM_DEFAULT)
        page++
        val tweetsResponse = getTweetsRequest(page)
        tweetsResponse
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnError {
                Log.e(LOG_TAG, "loadMoreFeed error: ${it.localizedMessage}")
                emit.onNext(error(it.localizedMessage!!, null))
            }.subscribe {
                if (it.isNotEmpty()) {
                    dao.addTweets(it)
                    sharedPreferences.edit().putInt(FEED_LIST_PAGE_NUM_KEY, page).apply()
                    emit.onNext(success(null))
                }
            }
    }

    private fun getTweetsRequest(page: Int) = service.getTweets(
        listType = FEED_LIST_TYPE,
        listId = FEED_LIST_ID,
        count = FEED_LIST_SIZE,
        page = page.toString()
    )

}