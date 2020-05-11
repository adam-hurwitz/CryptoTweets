package app.cryptotweets.feed.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo

data class User(
    @ColumnInfo(name = "userId") val id: Long,
    val screen_name: String,
    val profile_image_url_https: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(screen_name)
        parcel.writeString(profile_image_url_https)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel) = User(parcel)
        override fun newArray(size: Int) = arrayOfNulls<User?>(size)
    }
}