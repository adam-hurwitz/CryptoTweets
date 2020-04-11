package app.cryptotweets.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.room.Converters
import app.cryptotweets.feed.room.FeedDao

@Database(entities = [Tweet::class], version = 1)
@TypeConverters(Converters::class)
abstract class CryptoTweetsDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
}