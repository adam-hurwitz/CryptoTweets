package app.cryptotweets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import app.cryptotweets.databinding.FragmentTweetDetailBinding

class TweetDetailFragment : Fragment() {

    val args: TweetDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTweetDetailBinding.inflate(inflater)
        binding.user = args.user
        return binding.root
    }
}
