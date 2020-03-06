package app.cryptotweets.feed

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(private val feedService: FeedService) {

    suspend fun getTweets() = feedService.getTweets()

}