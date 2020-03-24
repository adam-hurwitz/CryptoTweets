package app.cryptotweets.feed.network

interface RepositoryLoadingCallback {
    fun onZeroItemsLoaded()
    fun onItemEndLoaded()
}