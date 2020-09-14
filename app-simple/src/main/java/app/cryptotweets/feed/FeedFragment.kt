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
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed.feed
import kotlinx.android.synthetic.main.fragment_feed.progressBar
import kotlinx.android.synthetic.main.fragment_feed.recyclerView
import kotlinx.android.synthetic.main.fragment_feed.swipeToRefresh
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class FeedFragment : Fragment(R.layout.fragment_feed) {

    lateinit var viewModel: FeedViewModel
    lateinit var adapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: FeedViewModel by viewModels {
            FeedViewModelFactory(
                owner = this,
                feedRepository = FeedRepository()
            )
        }
        this.viewModel = viewModel
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewStates()
    }

    private fun initAdapter() {
        adapter = FeedAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun initViewStates() {
        viewModel.feed.onEach { pagedList ->
            adapter.submitData(pagedList)
        }.launchIn(lifecycleScope)
        adapter.addLoadStateListener { loadState ->
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            swipeToRefresh.isRefreshing = loadState.source.append is LoadState.Loading
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            val errorState = loadState.source.refresh as? LoadState.Error
            errorState?.let {
                val snackbar =
                    Snackbar.make(feed, R.string.feed_error_message, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.feed_error_retry, onRetryListener())
                val textView =
                    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                snackbar.show()
            }
        }
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeToRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun onRetryListener() = OnClickListener {
        adapter.retry()
    }

}

