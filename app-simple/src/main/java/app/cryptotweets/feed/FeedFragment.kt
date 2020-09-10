package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.feed.viewmodel.FeedViewEvent
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import app.cryptotweets.utils.onEachEvent
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

    lateinit var app: App
    lateinit var viewModel: FeedViewModel
    lateinit var viewEvent: FeedViewEvent
    lateinit var adapter: FeedAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app = (context.applicationContext as App)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = FeedRepository(
            sharedPreferences = app.sharedPreferences,
            dao = app.dao,
            api = app.feedApi
        )
        val viewModel: FeedViewModel by viewModels {
            FeedViewModelFactory(
                owner = this,
                feedRepository = repository
            )
        }
        this.viewModel = viewModel
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

    private fun initViewStates() {
        viewModel.viewState.feed.onEach { pagedList ->
            adapter.submitList(pagedList)
        }.launchIn(lifecycleScope)
    }

    private fun initViewEffects() {
        viewModel.viewEffect.isLoading.onEachEvent { isLoading ->
            if (isLoading == true) {
                progressBar.visibility = VISIBLE
            } else {
                progressBar.visibility = GONE
                swipeToRefresh.isRefreshing = false
            }
        }.launchIn(lifecycleScope)

        viewModel.viewEffect.isError.onEachEvent {
            val snackbar = Snackbar.make(feed, R.string.feed_error_message, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.feed_error_retry, onRretryListener())
            val textView =
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            snackbar.show()
        }.launchIn(lifecycleScope)
    }

    private fun onRretryListener() = OnClickListener {
        viewEvent.retryEvent()
    }

    private fun initSwipeToRefresh() {
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeToRefresh.setOnRefreshListener {
            viewEvent.swipeToRefreshEvent()
        }
    }

}

