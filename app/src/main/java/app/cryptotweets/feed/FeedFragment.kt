package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class FeedFragment : Fragment(R.layout.fragment_feed) {
    private val LOG = FeedFragment::class.java.simpleName

    @Inject
    lateinit var repository: FeedRepository
    lateinit var adapter: FeedAdapter

    @ExperimentalCoroutinesApi
    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(owner = this, feedRepository = repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initViewState()
        initViewEffects()
    }

    private fun initAdapters() {
        adapter = FeedAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun initViewState() {
        viewModel.viewState.feed.observe(viewLifecycleOwner) { pagedList ->
            adapter.submitList(pagedList)
        }
    }

    private fun initViewEffects() {
        viewModel.viewEffects.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) progressBar.visibility = VISIBLE
            else progressBar.visibility = GONE
        }
    }

}

