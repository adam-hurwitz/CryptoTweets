package app.cryptotweets.Utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import app.cryptotweets.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.tweet_cell.view.*
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(url).also {
                transformImage(it)
            }
            .into(this)
            .clearOnDetach()
    }
}

private fun ImageView.transformImage(it: RequestBuilder<Drawable>) {
    if (this.id == userImage?.id)
        it.apply(GlideOptions.circleCropTransform())
    else if (this.id == mediaImage?.id)
        it.transform(RoundedCorners(MEDIA_RADIUS_INT))
}

@BindingAdapter("timestamp")
fun TextView.setTimestamp(createdAt: String) {
    val twitterFormat = context.getString(R.string.twitter_date_time_format)
    val simpleDateFormat = SimpleDateFormat(twitterFormat, Locale.getDefault())
    val date = simpleDateFormat.parse(createdAt)
    simpleDateFormat.applyPattern(context.getString(R.string.cryptotweets_date_time_format))
    this.text = simpleDateFormat.format(date)
}