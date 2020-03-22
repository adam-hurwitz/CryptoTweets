package app.cryptotweets.Utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.tweet_cell.view.*

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
