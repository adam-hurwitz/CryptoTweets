package app.cryptotweets.feed.models

import app.cryptotweets.feed.models.entities.Entities

//TODO: V2 get link generated images.
data class Tweet(
    val created_at: String,
    val id: Long,
    val text: String,
    val user: User,
    val entities: Entities
)
