package app.cryptotweets.feed.network

import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.utils.LIST_COUNT_QUERY
import app.cryptotweets.utils.LIST_ID_QUERY
import app.cryptotweets.utils.LIST_PAGE_NUM_QUERY
import app.cryptotweets.utils.LIST_TYPE_PATH
import app.cryptotweets.utils.TWITTER_API_PATH_URL
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedService {
    @GET(TWITTER_API_PATH_URL)
    fun getTweets(
        @Path(LIST_TYPE_PATH) listType: String,
        @Query(LIST_ID_QUERY) listId: String,
        @Query(LIST_COUNT_QUERY) count: String,
        @Query(LIST_PAGE_NUM_QUERY) page: String
    ): Observable<List<Tweet>>
}