package app.cryptotweets.feed.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.cryptotweets.feed.models.Tweet

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed")
    suspend fun getAll(): List<Tweet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tweets: List<Tweet>?)
}