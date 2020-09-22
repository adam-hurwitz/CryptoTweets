package app.cryptotweets.feed.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.cryptotweets.feed.models.Tweet

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTweets(tweets: List<Tweet>?)

    @Query("SELECT * FROM feed ORDER BY tweetId DESC")
    fun getAllTweets(): PagingSource<Int, Tweet>

    @Query("DELETE FROM feed")
    suspend fun clearTweets()
}