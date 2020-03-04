package app.cryptotweets

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun inject(feedFragment: FeedFragment)
}