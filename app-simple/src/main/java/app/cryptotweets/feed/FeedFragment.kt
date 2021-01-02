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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.cryptotweets.R
import app.cryptotweets.databinding.FragmentFeedBinding
import app.cryptotweets.feed.adapter.FeedAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val viewModel: FeedViewModel by viewModels()
    lateinit var adapter: FeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFeedBinding.bind(view)
        initViewStates(binding)
    }

    private fun initViewStates(binding: FragmentFeedBinding) {
        initAdapter(binding.recyclerView)
        initSwipeToRefresh(binding.swipeToRefresh)
        viewModel.feed.onEach { pagingData ->
            adapter.submitData(pagingData)
        }.launchIn(lifecycleScope)
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.swipeToRefresh.isRefreshing = loadState.source.append is LoadState.Loading
            binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            (loadState.source.refresh as? LoadState.Error)?.let {
                val snackbar = Snackbar.make(
                    binding.coordinatorLayout, R.string.feed_error_message, Snackbar.LENGTH_LONG
                )
                snackbar.setAction(R.string.feed_error_retry, onRetryListener())
                val textView =
                    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                snackbar.show()
            }
        }
    }

    private fun initAdapter(recyclerView: RecyclerView) {
        adapter = FeedAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun initSwipeToRefresh(swipeToRefresh: SwipeRefreshLayout) {
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeToRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun onRetryListener() = OnClickListener {
        adapter.retry()
    }

}

