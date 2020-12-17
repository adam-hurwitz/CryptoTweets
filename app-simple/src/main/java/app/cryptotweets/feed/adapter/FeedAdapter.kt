package app.cryptotweets.feed.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.cryptotweets.R
import app.cryptotweets.databinding.CellToptweetBinding
import app.cryptotweets.databinding.CellTweetBinding
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

class FeedAdapter : PagingDataAdapter<FeedCell, RecyclerView.ViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<FeedCell>() {
            override fun areItemsTheSame(oldItem: FeedCell, newItem: FeedCell) =
                (oldItem is FeedCell.TweetCell && newItem is FeedCell.TweetCell
                        && oldItem.tweet.id == newItem.tweet.id)
                        || (oldItem is FeedCell.TopTweetCell && newItem is FeedCell.TopTweetCell
                        && oldItem.textResource == newItem.textResource)

            override fun areContentsTheSame(oldItem: FeedCell, newItem: FeedCell) =
                oldItem == newItem
        }
    }

    class TweetViewHolder(private val binding: CellTweetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tweet: Tweet, onClickListener: View.OnClickListener) {
            binding.tweetCard.setOnClickListener(onClickListener)
            binding.userImage.load(tweet.user.profile_image_url_https) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_user_24)
            }
            binding.userImage.setOnClickListener(onClickListener)
            binding.screenName.text =
                String.format(
                    binding.screenName.context.getString(R.string.screen_name_at),
                    tweet.user.screen_name
                )
            binding.screenName.setOnClickListener(onClickListener)
            binding.tweetText.text = tweet.text
            binding.tweetText.setOnClickListener(onClickListener)
            binding.mediaImage.visibility =
                if (tweet.entities.media?.get(0)?.media_url_https != null) View.VISIBLE
                else View.GONE
            binding.mediaImage.load(tweet.entities.media?.get(0)?.media_url_https) {
                crossfade(true)
                placeholder(R.drawable.placeholder)
                transformations(RoundedCornersTransformation(MEDIA_RADIUS_INT))
                error(R.drawable.ic_media_24)
            }
            binding.timestamp.setTimestamp(tweet.created_at)
        }
    }

    class TopTweetViewHolder(
        private val binding: CellToptweetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topTweetCell: FeedCell.TopTweetCell) {
            binding.topTweetText.text = String.format(
                binding.topTweetText.context.getString(R.string.top_tweet_text),
                topTweetCell.favoriteCount
            )
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is FeedCell.TweetCell -> R.layout.cell_tweet
        is FeedCell.TopTweetCell -> R.layout.cell_toptweet
        null -> throw UnsupportedOperationException("Unknown view")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.cell_tweet) {
            val inflater = LayoutInflater.from(parent.context)
            return TweetViewHolder(CellTweetBinding.inflate(inflater, parent, false))
        } else {
            val inflater = LayoutInflater.from(parent.context)
            return TopTweetViewHolder(CellToptweetBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is FeedCell.TweetCell -> (holder as TweetViewHolder).bind(
                    it.tweet,
                    onClickListener(it.tweet)
                )
                is FeedCell.TopTweetCell -> (holder as TopTweetViewHolder).bind(it)
            }
        }
    }

    private fun onClickListener(tweet: Tweet) = View.OnClickListener { view ->
        val userAction =
            FeedFragmentDirections.actionFeedFragmentToProfileFragment(
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
                if (intent.resolveActivity(view.context.packageManager) != null)
                    startActivity(view.context, intent, null)
            }
        }
    }
}