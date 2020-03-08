package app.cryptotweets.feed.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.cryptotweets.feed.models.Tweet

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed")
    fun getAll(): List<Tweet>

    @Insert
    fun insertAll(vararg tweets: Tweet)
}