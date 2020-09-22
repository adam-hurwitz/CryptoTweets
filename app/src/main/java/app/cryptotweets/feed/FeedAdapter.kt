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
import app.cryptotweets.databinding.TweetCellBinding
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.utils.TWEET_BASE_URL
import app.cryptotweets.utils.TWEET_PATH_STATUS_URL
import app.cryptotweets.utils.TWEET_URL_PATTERN

val DIFF_UTIL = object : DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem == newItem
}

class FeedAdapter(
    val context: Context
) : PagingDataAdapter<Tweet, FeedAdapter.ViewHolder>(DIFF_UTIL) {

    class ViewHolder(private val binding: TweetCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tweet: Tweet, onClickListener: View.OnClickListener) {
            binding.tweet = tweet
            binding.onClick = onClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(TweetCellBinding.inflate(inflater, parent, false))
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