package app.cryptotweets.feed.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.cryptotweets.feed.models.Tweet
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed")
    fun getAll(): Flow<List<Tweet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tweets: List<Tweet>?)
}