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

class FeedAdapter(private val context: Context) :
    PagingDataAdapter<Tweet, FeedAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Tweet>() {
            override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tweetCardView = view.findViewById<ConstraintLayout>(R.id.tweetCard)
        val userImageView = view.findViewById<ImageView>(R.id.userImage)
        val tweetTextView = view.findViewById<TextView>(R.id.tweetText)
        val screenNameView = view.findViewById<TextView>(R.id.screenName)
        val mediaImageView = view.findViewById<ImageView>(R.id.mediaImage)
        val timestampView = view.findViewById<TextView>(R.id.timestamp)
        fun bind(tweet: Tweet, onClickListener: View.OnClickListener) {
            tweetCardView.setOnClickListener(onClickListener)
            userImageView.load(tweet.user.profile_image_url_https) {
                transformations(CircleCropTransformation())
            }
            userImageView.setOnClickListener(onClickListener)
            tweetTextView.text = tweet.text
            tweetTextView.setOnClickListener(onClickListener)
            screenNameView.text = String.format(
                context.getString(R.string.screen_name_at), tweet.user.screen_name
            )
            screenNameView.setOnClickListener(onClickListener)
            if (tweet.entities.media != null) {
                mediaImageView.visibility = View.VISIBLE
                mediaImageView.load(tweet.entities.media.get(0).media_url_https) {
                    transformations(RoundedCornersTransformation(MEDIA_RADIUS_INT))
                }
                mediaImageView.setOnClickListener(onClickListener)
            } else mediaImageView.visibility = View.GONE
            timestampView.setTimestamp(tweet.created_at)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.cell_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClickListener(it))
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
            R.id.tweetCard, R.id.tweetText, R.id.mediaImage -> {
                val url = String.format(
                    TWEET_URL_PATTERN, TWEET_BASE_URL,
                    tweet.user.screen_name,
                    TWEET_PATH_STATUS_URL,
                    tweet.id
                )
                val webpage: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                if (intent.resolveActivity(view.context.packageManager) != null)
                    startActivity(view.context, intent, null)
            }
        }
    }
}