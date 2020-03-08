package app.cryptotweets.feed.models.entities

import androidx.room.Embedded

//FIXME
data class Entities(
    @Embedded val urls: List<Urls?>
    /*,
    @Embedded val media: List<Media>*/
)