package app.cryptotweets

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import app.cryptotweets.feed.network.FeedApi
import app.cryptotweets.feed.room.FeedDao
import app.cryptotweets.utils.AUTHORIZATION_KEY
import app.cryptotweets.utils.CRYPTOTWEETS_DATABASE_NAME
import app.cryptotweets.utils.CRYPTOTWEETS_SHARED_PREF
import app.cryptotweets.utils.CryptoTweetsDatabase
import app.cryptotweets.utils.TWITTER_API_BASE_URL
import app.cryptotweets.utils.auth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var dao: FeedDao
    lateinit var feedApi: FeedApi

    override fun onCreate() {
        super.onCreate()
        sharedPreferences =
            this.getSharedPreferences(CRYPTOTWEETS_SHARED_PREF, Context.MODE_PRIVATE)
        dao = Room.databaseBuilder(
            this,
            CryptoTweetsDatabase::class.java,
            CRYPTOTWEETS_DATABASE_NAME
        ).build().feedDao()
        feedApi = Retrofit.Builder()
            .baseUrl(TWITTER_API_BASE_URL)
            .client(providesOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedApi::class.java)
    }

    fun providesOkHttpClient() = OkHttpClient()
        .newBuilder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .header(AUTHORIZATION_KEY, auth)
                    .build()
                return chain.proceed(request)
            }
        }).build()
}