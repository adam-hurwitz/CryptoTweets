package app.cryptotweets.feed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.cryptotweets.App
import app.cryptotweets.R
import app.cryptotweets.viewmodel.FeedViewModel
import app.cryptotweets.viewmodel.FeedViewModelFactory
import javax.inject.Inject

class FeedFragment : Fragment() {

    @Inject
    lateinit var feedRepository: FeedRepository

    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(owner = this, feedRepository = feedRepository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

}
