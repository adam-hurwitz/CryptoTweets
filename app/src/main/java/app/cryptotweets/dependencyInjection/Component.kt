package app.cryptotweets.dependencyInjection

import app.cryptotweets.feed.FeedFragment
import app.cryptotweets.feed.FeedRepository
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface AppComponent {
    fun inject(feedFragment: FeedFragment)

    fun retrofit(): Retrofit
    fun feedRepository(): FeedRepository
}