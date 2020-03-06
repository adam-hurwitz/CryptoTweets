package app.cryptotweets.dependencyInjection

import app.cryptotweets.Utils.AUTHORIZATION_KEY
import app.cryptotweets.Utils.BASE_URL
import app.cryptotweets.auth
import app.cryptotweets.feed.FeedService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class UtilsModule {

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
}