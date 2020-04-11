package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.feed.adapter.FeedAdapter
import app.cryptotweets.feed.viewmodel.FeedViewEvent
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class FeedFragment : Fragment(R.layout.fragment_feed) {

    @Inject
    lateinit var repository: FeedRepository
    lateinit var viewEvent: FeedViewEvent
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
        viewModel.launchViewEvents(this)
        initAdapter()
        initViewStates()
        initViewEffects()
        initSwipeToRefresh()
    }

    fun attachViewEvents(viewEvent: FeedViewEvent) {
        this.viewEvent = viewEvent
    }

    private fun initAdapter() {
        adapter = FeedAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun initViewStates() {
        viewModel.viewState.feed.observe(viewLifecycleOwner) { pagedList ->
            adapter.submitList(pagedList)
        }
    }

    @ExperimentalCoroutinesApi
    private fun initViewEffects() {
        viewModel.viewEffect.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) progressBar.visibility = VISIBLE
            else {
                progressBar.visibility = GONE
                swipeToRefresh.isRefreshing = false
            }
        }
        viewModel.viewEffect.isError.observe(viewLifecycleOwner) { isError ->
            val snackbar = Snackbar.make(feed, R.string.feed_error_message, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.feed_error_retry, onRretryListener())
            val textView =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            snackbar.show()
        }
    }

    private fun onRretryListener() = View.OnClickListener {
        viewEvent.retryEvent()
    }

    private fun initSwipeToRefresh() {
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeToRefresh.setOnRefreshListener {
            viewEvent.swipeToRefreshEvent()
        }
    }

}

