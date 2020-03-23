package app.cryptotweets.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.cryptotweets.databinding.TweetCellBinding
import app.cryptotweets.feed.models.Tweet
import kotlinx.coroutines.ExperimentalCoroutinesApi

val DIFF_UTIL = object : DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet) = oldItem == newItem
}

class FeedAdapter : PagedListAdapter<Tweet, FeedAdapter.FeedViewHolder>(DIFF_UTIL) {
    //TODO: Update notes for "binding: ViewDataBinding"
    class FeedViewHolder(private val binding: TweetCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @ExperimentalCoroutinesApi
        fun bind(tweet: Tweet) {
            //TODO
            /*fun bind(
                feedViewModel: FeedViewModel,
                tweet: Tweet,
                onClickListener: View.OnClickListener
            ) {*/
            binding.tweet = tweet
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        //TODO: LayoutInflater, binding, inflate
        val inflater = LayoutInflater.from(parent.context)
        return FeedViewHolder(TweetCellBinding.inflate(inflater, parent, false))
    }

    @ExperimentalCoroutinesApi
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        //TODO: feedViewModel, tweet, onClickListener
        holder.bind(getItem(position) as Tweet)
    }

}