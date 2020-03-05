package app.cryptotweets.dependencyInjection

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UtilsModule {

    @Singleton
    @Provides
    fun providesRetrofit() =
        Retrofit.Builder()
            .baseUrl("https://api.twitter.com/1.1/lists/statuses.json?list_id=1173707664863350785&count=10")
            .build()
}