package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class FeedFragment : Fragment(R.layout.fragment_feed) {

    @Inject
    lateinit var repository: FeedRepository
    lateinit var adapter: FeedAdapter

    @ExperimentalCoroutinesApi
    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(owner = this, feedRepository = repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).component.inject(this)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val coordinatorLayout = view.findViewById<CoordinatorLayout>(R.id.feed)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val swipeToRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        initAdapter(recyclerView)
        initViewStates(coordinatorLayout, progressBar, swipeToRefresh)
        initSwipeToRefresh(swipeToRefresh)
    }

    private fun initAdapter(recyclerView: RecyclerView) {
        adapter = FeedAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun initViewStates(
        coordinatorLayout: CoordinatorLayout,
        progressBar: ProgressBar,
        swipeToRefresh: SwipeRefreshLayout
    ) {
        viewModel.viewState.feed.onEach { pagedList ->
            adapter.submitData(pagedList)
        }.launchIn(lifecycleScope)
        adapter.addLoadStateListener { loadState ->
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            swipeToRefresh.isRefreshing =
                loadState.source.append is LoadState.Loading
                        && loadState.source.refresh is LoadState.Loading
            val errorState = loadState.source.refresh as? LoadState.Error
            errorState?.let {
                val snackbar = Snackbar.make(
                    coordinatorLayout,
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

    private fun onRetryListener() = OnClickListener {
        adapter.retry()
    }

    private fun initSwipeToRefresh(swipeToRefresh: SwipeRefreshLayout) {
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeToRefresh.setOnRefreshListener { adapter.refresh() }
    }

}

