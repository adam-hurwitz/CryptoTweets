package app.cryptotweets.feed.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remoteKeys")
data class RemoteKeys(
        @PrimaryKey val tweetId: Long,
        val prevKey: Int?,
        val nextKey: Int?
)
