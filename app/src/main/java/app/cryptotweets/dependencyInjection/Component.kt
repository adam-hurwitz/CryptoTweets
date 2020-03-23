package app.cryptotweets.dependencyInjection

import app.cryptotweets.feed.FeedFragment
import app.cryptotweets.feed.FeedRepository
import app.cryptotweets.feed.network.FeedService
import app.cryptotweets.feed.room.FeedDao
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
    fun feedDao(): FeedDao
}