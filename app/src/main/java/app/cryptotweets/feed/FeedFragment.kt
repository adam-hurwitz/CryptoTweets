package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.databinding.FragmentFeedBinding
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.core.view.isVisible
import androidx.paging.ExperimentalPagingApi
import javax.inject.Inject

@ExperimentalPagingApi
class FeedFragment : Fragment(R.layout.fragment_feed) {

    @Inject
    lateinit var repository: FeedRepository
    lateinit var adapter: FeedAdapter

    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(owner = this, feedRepository = repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFeedBinding.bind(view)
        initViewStates(binding)
    }

    private fun initViewStates(binding: FragmentFeedBinding) {
        initAdapter(binding.recyclerView)
        initSwipeToRefresh(binding.swipeToRefresh)
        viewModel.viewState.feed.onEach { pagedList ->
            adapter.submitData(pagedList)
        }.launchIn(lifecycleScope)
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.swipeToRefresh.isRefreshing =
                loadState.source.append is LoadState.Loading
                        && loadState.source.refresh is LoadState.Loading
            val errorState = loadState.source.refresh as? LoadState.Error
            errorState?.let {
                val snackbar = Snackbar.make(
                    binding.coordinatorLayout,
                    R.string.feed_error_message,
                    Snackbar.LENGTH_LONG
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
        adapter = FeedAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
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

