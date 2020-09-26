package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.feed.viewmodel.FeedViewEvent
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_feed.feed
import kotlinx.android.synthetic.main.fragment_feed.progressBar
import kotlinx.android.synthetic.main.fragment_feed.recyclerView
import kotlinx.android.synthetic.main.fragment_feed.swipeToRefresh
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class FeedFragment : Fragment(R.layout.fragment_feed) {
    val LOG_TAG = FeedFragment::class.java.simpleName

    @Inject
    lateinit var repository: FeedRepository
    lateinit var viewEvent: FeedViewEvent
    lateinit var adapter: FeedAdapter

    private val compositeDisposable = CompositeDisposable()

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

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        compositeDisposable.dispose()
        viewModel.clearAndDisposeDisposables()
    }

    fun attachViewEvents(viewEvent: FeedViewEvent) {
        this.viewEvent = viewEvent
    }

    private fun initAdapter() {
        adapter = FeedAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun initViewStates() {
        val disposable = viewModel.viewState.feed
            .doOnError { Log.v(LOG_TAG, "Error loading pagedList") }
            .subscribe { pagedList ->
                adapter.submitList(pagedList)
            }
        compositeDisposable.add(disposable)
    }

    @ExperimentalCoroutinesApi
    private fun initViewEffects() {
        val isLoadingDisposable = viewModel.viewEffect.isLoading
            .doOnError { Log.v(LOG_TAG, "Error loading isLoading") }
            .subscribe { isLoading ->
                if (isLoading.getContentIfNotHandled() == true) {
                    progressBar.visibility = VISIBLE
                } else {
                    progressBar.visibility = GONE
                    swipeToRefresh.isRefreshing = false
                }
            }
        val isErrorDisposable = viewModel.viewEffect.isError
            .doOnError { Log.v(LOG_TAG, "Error loading isError") }
            .subscribe { isError ->
                if (isError.getContentIfNotHandled() == true) {
                    val snackbar =
                            Snackbar.make(feed, R.string.feed_error_message, Snackbar.LENGTH_LONG)
                    snackbar.setAction(R.string.feed_error_retry, onRretryListener())
                    val textView =
                            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    snackbar.show()
                }
            }
        compositeDisposable.addAll(isLoadingDisposable, isErrorDisposable)
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

