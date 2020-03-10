package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.feed.viewmodel.FeedViewModel
import app.cryptotweets.feed.viewmodel.FeedViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedFragment : Fragment() {
    private val LOG = FeedFragment::class.java.simpleName

    @Inject
    lateinit var feedRepository: FeedRepository

    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(owner = this, feedRepository = feedRepository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.feedViewState.feed.observe(viewLifecycleOwner){ feed ->
            Log.v(LOG, "Tweets in view: " + feed)
        }
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

}

