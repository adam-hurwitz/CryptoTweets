package app.cryptotweets.feed.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import app.cryptotweets.feed.FeedFragment
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.feed.network.FeedRepository
import app.cryptotweets.utils.Event
import app.cryptotweets.utils.Status.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FeedViewModel(private val feedRepository: FeedRepository) : ViewModel(), FeedViewEvent {
    val LOG_TAG = FeedViewModel::class.java.simpleName

    private val disposables = CompositeDisposable()
    private val _viewState = _FeedViewState()
    val viewState = FeedViewState(_viewState)
    private val _viewEffect = _FeedViewEffect()
    val viewEffect = FeedViewEffect(_viewEffect)

    init {
        Log.v(LOG_TAG, "init")
        initFeed(true)
    }

    fun launchViewEvents(fragment: FeedFragment) {
        fragment.attachViewEvents(this)
    }

    fun clearAndDisposeDisposables() {
        disposables.clear()
        disposables.dispose()
    }

    override fun swipeToRefreshEvent() {
        initFeed(true)
    }

    override fun retryEvent() {
        initFeed(true)
    }

    private fun initFeed(toRetry: Boolean) {
        val disposable = feedRepository.initFeed(pagedListBoundaryCallback(toRetry))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { results ->
                when (results.status) {
                    LOADING -> {
                        Log.v(LOG_TAG, "initFeed ${LOADING.name}")
                        _viewEffect._isLoading.onNext(Event(true))
                    }
                    SUCCESS -> {
                        Log.v(LOG_TAG, "initFeed ${SUCCESS.name}")
                        _viewEffect._isLoading.onNext(Event(false))
                        _viewState._feed.onNext(results.data)
                    }
                    ERROR -> {
                        Log.v(LOG_TAG, "initFeed ${ERROR.name}")
                        _viewEffect._isLoading.onNext(Event(false))
                        _viewEffect._isError.onNext(Event(true))
                    }
                }
            }
        disposables.add(disposable)
    }

    private fun pagedListBoundaryCallback(toRetry: Boolean) =
        object : PagedList.BoundaryCallback<Tweet>() {

            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                if (toRetry) initFeed(false)
            }

            override fun onItemAtEndLoaded(itemAtEnd: Tweet) {
                super.onItemAtEndLoaded(itemAtEnd)
                val disposable = feedRepository.loadMoreFeed()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe { results ->
                        when (results.status) {
                            LOADING -> Log.v(LOG_TAG, "onItemEndLoaded LOADING")
                            SUCCESS -> Log.v(LOG_TAG, "onItemEndLoaded SUCCESS")
                            ERROR -> {
                                Log.v(LOG_TAG, "onItemEndLoaded ERROR")
                                _viewEffect._isError.onNext(Event(true))
                            }
                        }
                    }
                disposables.add(disposable)
            }
        }
}