package app.cryptotweets.feed.models

data class Tweet(val created_at: String, val id: Long, val text: String, val user: User)
//TODO
// entities Obj > media Array
//  url String
//  media_url String
// retweeted_status Obj > entities Obj > urls Array > url String
//  add images in v2
