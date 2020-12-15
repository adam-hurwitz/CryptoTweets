package app.cryptotweets.utils

import android.widget.TextView
import app.cryptotweets.R
import java.text.SimpleDateFormat
import java.util.*

fun TextView.setTimestamp(createdAt: String?) {
    createdAt?.let {
        val twitterFormat = context.getString(R.string.twitter_date_time_format)
        val simpleDateFormat = SimpleDateFormat(twitterFormat, Locale.getDefault())
        val date = simpleDateFormat.parse(createdAt)
        simpleDateFormat.applyPattern(context.getString(R.string.cryptotweets_date_time_format))
        this.text = simpleDateFormat.format(date)
    }
}
