package app.cryptotweets.feed.network

import app.cryptotweets.Utils.LIST_COUNT_QUERY
import app.cryptotweets.Utils.LIST_ID_QUERY
import app.cryptotweets.Utils.LIST_TYPE_PATH
import app.cryptotweets.feed.models.Tweet
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedService {
    @GET("lists/{listType}")
    suspend fun getTweets(
        @Path(LIST_TYPE_PATH) listType: String,
        @Query(LIST_ID_QUERY) listId: String,
        @Query(LIST_COUNT_QUERY) count: String
    ): List<Tweet>

    //TODO: Refactor
    @GET("lists/{listType}")
    suspend fun getTweets2(
        @Path(LIST_TYPE_PATH) listType: String,
        @Query(LIST_ID_QUERY) listId: String,
        @Query(LIST_COUNT_QUERY) count: String,
        @Query("page") page: String
    ): List<Tweet>
}