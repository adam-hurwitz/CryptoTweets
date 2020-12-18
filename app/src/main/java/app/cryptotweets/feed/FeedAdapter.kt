package app.cryptotweets.feed

import android.content.Context
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
import app.cryptotweets.databinding.CellTweetBinding
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.utils.MEDIA_RADIUS_INT
import app.cryptotweets.utils.TWEET_BASE_URL
import app.cryptotweets.utils.TWEET_PATH_STATUS_URL
import app.cryptotweets.utils.TWEET_URL_PATTERN
import coil.transform.CircleCropTransformation
import coil.load
import coil.transform.RoundedCornersTransformation
import app.cryptotweets.utils.setTimestamp

class FeedAdapter(private val context: Context) :
    PagingDataAdapter<Tweet, FeedAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Tweet>() {
            override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: CellTweetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tweet: Tweet, onClickListener: View.OnClickListener) {
            binding.tweetCard.setOnClickListener(onClickListener)
            binding.userImage.load(tweet.user.profile_image_url_https) {
                transformations(CircleCropTransformation())
            }
            binding.userImage.setOnClickListener(onClickListener)
            binding.tweetText.text = tweet.text
            binding.tweetText.setOnClickListener(onClickListener)
            binding.screenName.text = String.format(
                context.getString(R.string.screen_name_at), tweet.user.screen_name
            )
            binding.screenName.setOnClickListener(onClickListener)
            if (tweet.entities.media != null) {
                binding.mediaImage.visibility = View.VISIBLE
                binding.mediaImage.load(tweet.entities.media.get(0).media_url_https) {
                    transformations(RoundedCornersTransformation(MEDIA_RADIUS_INT))
                }
                binding.mediaImage.setOnClickListener(onClickListener)
            } else binding.mediaImage.visibility = View.GONE
            binding.timestamp.setTimestamp(tweet.created_at)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(CellTweetBinding.inflate(inflater, parent, false))
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