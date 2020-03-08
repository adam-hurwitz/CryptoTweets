package app.cryptotweets.feed.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.cryptotweets.feed.models.Tweet

@Database(entities = arrayOf(Tweet::class), version = 1)
@TypeConverters(Converters::class)
abstract class FeedDatabase: RoomDatabase() {
    abstract fun feedDao(): FeedDao
}