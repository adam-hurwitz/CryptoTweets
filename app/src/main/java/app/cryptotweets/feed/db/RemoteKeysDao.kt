package app.cryptotweets.feed.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remoteKeys WHERE tweetId = :repoId")
    suspend fun remoteKeysTweetId(repoId: Long): RemoteKeys?

    @Query("DELETE FROM remoteKeys")
    suspend fun clearRemoteKeys()
}

