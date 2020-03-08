package app.cryptotweets.feed.room

import androidx.room.TypeConverter
import app.cryptotweets.feed.models.entities.Media
import app.cryptotweets.feed.models.entities.Urls
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toUrlsString(value: List<Urls>) = Gson().toJson(value)

    @TypeConverter
    fun toUrlsList(value: String) =
        (Gson().fromJson(value, Array<Urls>::class.java)).toList()

    //TODO: Create StackOverflow for generic @TypeConverter
    /*@TypeConverter
    fun <T> toUrlsString(value: List<T>) = Gson().toJson(value)

    @TypeConverter
    fun <T> toUrlsList(value: String): List<T> =
        (Gson().fromJson(value, Array<T>::class.java)).toList()*/

    @TypeConverter
    fun toMediaString(value: List<Media>) = Gson().toJson(value)

    @TypeConverter
    fun toMediaList(value: String) =
        (Gson().fromJson(value, Array<Media>::class.java)).toList()

}