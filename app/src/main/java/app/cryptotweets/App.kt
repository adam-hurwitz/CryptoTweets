package app.cryptotweets

import android.app.Application
import app.cryptotweets.dependencyInjection.DaggerAppComponent
import app.cryptotweets.dependencyInjection.UtilsModule

class App : Application() {
    val component = DaggerAppComponent
        .builder()
        .utilsModule(UtilsModule(this))
        .build()
}