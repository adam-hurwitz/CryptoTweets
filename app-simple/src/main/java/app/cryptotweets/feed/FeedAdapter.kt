package app.cryptotweets.feed

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.cryptotweets.R
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.utils.MEDIA_RADIUS_INT
import app.cryptotweets.utils.TWEET_BASE_URL
import app.cryptotweets.utils.TWEET_PATH_STATUS_URL
import app.cryptotweets.utils.TWEET_URL_PATTERN
import app.cryptotweets.utils.setTimestamp
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

class FeedAdapter(
    private val context: Context
) : PagingDataAdapter<Tweet, FeedAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Tweet>() {
            override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tweetCard = view.findViewById<ConstraintLayout>(R.id.tweetCard)
        val userImage = view.findViewById<ImageView>(R.id.userImage)
        val screenName = view.findViewById<TextView>(R.id.screenName)
        val tweetText = view.findViewById<TextView>(R.id.tweetText)
        val mediaImage = view.findViewById<ImageView>(R.id.mediaImage)
        val timestamp = view.findViewById<TextView>(R.id.timestamp)

        fun bind(context: Context, tweet: Tweet, onClickListener: View.OnClickListener) {
            tweetCard.setOnClickListener(onClickListener)
            userImage.load(tweet.user.profile_image_url_https) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_user_24)
            }
            userImage.setOnClickListener(onClickListener)
            screenName.text =
                String.format(context.getString(R.string.screen_name_at), tweet.user.screen_name)
            screenName.setOnClickListener(onClickListener)
            tweetText.text = tweet.text
            tweetText.setOnClickListener(onClickListener)
            mediaImage.visibility =
                if (tweet.entities.media?.get(0)?.media_url_https != null) View.VISIBLE
                else View.GONE
            mediaImage.load(tweet.entities.media?.get(0)?.media_url_https) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                transformations(RoundedCornersTransformation(MEDIA_RADIUS_INT))
                error(R.drawable.ic_media_24)
            }
            timestamp.setTimestamp(tweet.created_at)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.tweet_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(context, it, onClickListener(it))
        }
    }

    private fun onClickListener(tweet: Tweet) = View.OnClickListener { view ->
        val userAction =
            FeedFragmentDirections.actionFeedFragmentToTweetDetailFragment(
                label = tweet.user.screen_name,
                user = tweet.user
            )
        when (view.id) {
            R.id.userImage, R.id.screenName -> view.findNavController().navigate(userAction)
            R.id.tweetCard, R.id.tweetText -> {
                val url = String.format(
                    TWEET_URL_PATTERN, TWEET_BASE_URL,
                    tweet.user.screen_name,
                    TWEET_PATH_STATUS_URL,
                    tweet.id
                )
                val webpage: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                if (intent.resolveActivity(context.packageManager) != null)
                    startActivity(context, intent, null)
            }
        }
    }
}