package app.cryptotweets.dependencyInjection

import app.cryptotweets.feed.FeedFragment
import app.cryptotweets.feed.FeedRepository
import app.cryptotweets.feed.FeedService
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface AppComponent {
    fun inject(feedFragment: FeedFragment)

    fun okHttpClient(): OkHttpClient
    fun retrofit(): Retrofit
    fun FeedService(): FeedService
    fun feedRepository(): FeedRepository
}