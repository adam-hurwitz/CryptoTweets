package app.cryptotweets.dependencyInjection

import android.app.Application
import android.content.Context
import androidx.room.Room
import app.cryptotweets.feed.network.FeedService
import app.cryptotweets.utils.*
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class UtilsModule(private val app: Application) {

    @Singleton
    @Provides
    fun providesSharedPreferences() =
        app.getSharedPreferences(CRYPTOTWEETS_SHARED_PREF, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesOkHttpClient() = OkHttpClient().newBuilder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .header(AUTHORIZATION_KEY, auth)
                    .build()
                return chain.proceed(request)
            }
        }).build()

    @Singleton
    @Provides
    fun providesService() = Retrofit.Builder()
        .baseUrl(TWITTER_API_BASE_URL)
        .client(providesOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(FeedService::class.java)

    @Singleton
    @Provides
    fun providesFeedDao() = Room.databaseBuilder(
        app,
        CryptoTweetsDatabase::class.java,
        CRYPTOTWEETS_DATABASE_NAME
    ).build().feedDao()
}