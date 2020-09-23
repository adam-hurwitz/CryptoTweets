package app.cryptotweets.feed.adapter

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
import app.cryptotweets.feed.FeedFragmentDirections
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
) : PagingDataAdapter<FeedCell, RecyclerView.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<FeedCell>() {
            override fun areItemsTheSame(oldItem: FeedCell, newItem: FeedCell) =
                (oldItem is FeedCell.TweetCell && newItem is FeedCell.TweetCell
                        && oldItem.tweet.id == newItem.tweet.id)
                        || (oldItem is FeedCell.TopTweetCell && newItem is FeedCell.TopTweetCell
                        && oldItem.text == newItem.text)

            override fun areContentsTheSame(oldItem: FeedCell, newItem: FeedCell) =
                oldItem == newItem
        }
    }

    class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    class TopTweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topTweetTextView = view.findViewById<TextView>(R.id.topTweetText)

        fun bind(topTweetText: String) {
            topTweetTextView.text = topTweetText
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is FeedCell.TweetCell -> R.layout.cell_tweet
        is FeedCell.TopTweetCell -> R.layout.cell_toptweet
        null -> throw UnsupportedOperationException("Unknown view")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.cell_tweet) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.cell_tweet, parent, false)
            return TweetViewHolder(view)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.cell_toptweet, parent, false)
            return TopTweetViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is FeedCell.TweetCell -> (holder as TweetViewHolder).bind(
                    context,
                    it.tweet,
                    onClickListener(it.tweet)
                )
                is FeedCell.TopTweetCell -> (holder as TopTweetViewHolder).bind(it.text)
            }
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