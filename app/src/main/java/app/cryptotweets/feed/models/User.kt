package app.cryptotweets.feed.models

import androidx.room.ColumnInfo

data class User(
    @ColumnInfo(name = "user_id") val id: Long,
    val screen_name: String,
    val profile_image_url_https: String
)