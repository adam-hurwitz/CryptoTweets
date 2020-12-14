package app.cryptotweets

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import app.cryptotweets.feed.ProfileFragmentArgs
import coil.load
import coil.transform.CircleCropTransformation


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val args: ProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userImage = view.findViewById<ImageView>(R.id.userImage)
        userImage.load(args.user.profile_image_url_https) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
            transformations(CircleCropTransformation())
            error(R.drawable.ic_user_24)
        }
    }
}
