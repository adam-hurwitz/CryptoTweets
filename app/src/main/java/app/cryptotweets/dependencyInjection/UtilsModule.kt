package app.cryptotweets.dependencyInjection

import android.app.Application
import android.content.Context
import androidx.room.Room
import app.cryptotweets.Utils.AUTHORIZATION_KEY
import app.cryptotweets.Utils.BASE_URL
import app.cryptotweets.Utils.CRYPTOTWEETS_DATABASE_NAME
import app.cryptotweets.Utils.CRYPTOTWEETS_SHARED_PREF
import app.cryptotweets.auth
import app.cryptotweets.feed.network.FeedService
import app.cryptotweets.feed.room.FeedDatabase
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
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
    fun providesOkHttpClient() = OkHttpClient.Builder()
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
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(providesOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providesFeedService() = providesRetrofit().create(FeedService::class.java)

    @Singleton
    @Provides
    fun providesFeedDatabase() = Room.databaseBuilder(
        app,
        FeedDatabase::class.java,
        CRYPTOTWEETS_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesFeedDao() = providesFeedDatabase().feedDao()
}