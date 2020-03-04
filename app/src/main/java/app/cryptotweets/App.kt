package app.cryptotweets

import android.app.Application

class App: Application() {
    val appComponent = DaggerAppComponent.create()
}