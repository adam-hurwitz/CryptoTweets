package app.cryptotweets.feed

import app.cryptotweets.feed.models.Tweet
import retrofit2.http.GET
import retrofit2.http.Path

interface FeedService {
    //TODO: Pass params in.
    @GET("lists/statuses.json?list_id=1173707664863350785&count=10")
    suspend fun getTweets(): List<Tweet>
}