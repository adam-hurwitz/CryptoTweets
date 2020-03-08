package app.cryptotweets.feed.room

import androidx.room.TypeConverter
import app.cryptotweets.feed.models.entities.Urls
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toUrlsString(value: List<Urls?>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toUrlsList(value: String): List<Urls?> {
        val objects = Gson().fromJson(value, Array<Urls?>::class.java) as Array<Urls?>
        val list = objects.toList()
        return list
    }

}