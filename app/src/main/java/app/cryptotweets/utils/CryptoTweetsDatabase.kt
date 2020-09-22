package app.cryptotweets.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.cryptotweets.feed.db.FeedDao
import app.cryptotweets.feed.db.RemoteKeys
import app.cryptotweets.feed.db.RemoteKeysDao
import app.cryptotweets.feed.db.RoomConverters
import app.cryptotweets.feed.models.Tweet

@Database(entities = [Tweet::class, RemoteKeys::class], version = 2)
@TypeConverters(RoomConverters::class)
abstract class CryptoTweetsDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}