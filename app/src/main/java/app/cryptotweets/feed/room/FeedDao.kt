package app.cryptotweets.feed.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.cryptotweets.feed.models.Tweet

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTweets(tweets: List<Tweet>?)

    @Query("SELECT * FROM feed ORDER BY tweetId DESC")
    fun getAllTweets(): DataSource.Factory<Int, Tweet>
}