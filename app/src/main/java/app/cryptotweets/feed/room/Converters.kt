package app.cryptotweets.feed.room

import androidx.room.TypeConverter
import app.cryptotweets.feed.models.entities.Media
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toMediaString(value: List<Media>?) = Gson().toJson(value)

    @TypeConverter
    fun toMediaList(value: String) = Gson().fromJson(value, Array<Media>::class.java)?.toList()

}