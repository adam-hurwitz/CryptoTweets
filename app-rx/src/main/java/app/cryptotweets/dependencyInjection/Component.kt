package app.cryptotweets.dependencyInjection

import app.cryptotweets.feed.FeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface AppComponent {
    fun inject(feedFragment: FeedFragment)
}