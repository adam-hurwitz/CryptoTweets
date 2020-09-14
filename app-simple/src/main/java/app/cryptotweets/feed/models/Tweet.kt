package app.cryptotweets.feed.models

import app.cryptotweets.feed.models.entities.Entities

data class Tweet(
    val id: Long,
    val created_at: String,
    val text: String,
    val user: User,
    val entities: Entities
)
