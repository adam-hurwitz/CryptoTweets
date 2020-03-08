package app.cryptotweets.feed.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.cryptotweets.feed.models.entities.Entities

//TODO: V2 get link generated images.
@Entity(tableName = "feed")
data class Tweet(
    @PrimaryKey @ColumnInfo(name = "tweet_id") val id: Long,
    @ColumnInfo(name = "created_at") val created_at: String,
    val text: String,
    @Embedded val user: User/*,
    @Embedded val entities: Entities*/ //FIXME
)
