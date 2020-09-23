package app.cryptotweets.feed.adapter

import app.cryptotweets.feed.models.Tweet

sealed class FeedCell {

    data class TweetCell(val tweet: Tweet) : FeedCell()

    data class TopTweetCell(val text: String) : FeedCell()
}