package app.cryptotweets.feed

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import app.cryptotweets.R
import app.cryptotweets.databinding.FragmentFeedBinding
import app.cryptotweets.feed.adapter.FeedAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by viewModels()
    lateinit var adapter: FeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFeedBinding.bind(view)
        initAdapter(binding)
        initViewStates(binding)
    }

    private fun initAdapter(binding: FragmentFeedBinding) {
        adapter = FeedAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun initViewStates(binding: FragmentFeedBinding) {
        viewModel.feed.onEach { pagingData ->
            adapter.submitData(pagingData)
        }.launchIn(lifecycleScope)
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.swipeToRefresh.isRefreshing = loadState.source.append is LoadState.Loading
            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            (loadState.source.refresh as? LoadState.Error)?.let {
                val snackbar =
                    Snackbar.make(binding.feed, R.string.feed_error_message, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.feed_error_retry, onRetryListener())
                val textView =
                    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                snackbar.show()
            }
        }
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        binding.swipeToRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun onRetryListener() = OnClickListener {
        adapter.retry()
    }

}

