package app.cryptotweets

import android.app.Application
import app.cryptotweets.dependencyInjection.DaggerComponent
import app.cryptotweets.dependencyInjection.UtilsModule

class App : Application() {
    val component = DaggerComponent
        .builder()
        .utilsModule(UtilsModule(this))
        .build()
}