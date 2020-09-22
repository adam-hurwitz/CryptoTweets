package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
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
import androidx.core.view.isVisible

class FeedFragment : Fragment() {

    @Inject
    lateinit var repository: FeedRepository
    lateinit var binding: app.cryptotweets.databinding.FragmentFeedBinding
    lateinit var adapter: FeedAdapter

    @ExperimentalCoroutinesApi
    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(owner = this, feedRepository = repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            app.cryptotweets.databinding.FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewStates()
        initSwipeToRefresh()
    }

    private fun initAdapter() {
        adapter = FeedAdapter(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun initViewStates() {
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
                    binding.feed,
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

    private fun initSwipeToRefresh() {
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        binding.swipeToRefresh.setOnRefreshListener { adapter.refresh() }
    }

}

