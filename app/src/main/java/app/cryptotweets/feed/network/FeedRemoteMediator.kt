package app.cryptotweets.feed.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import app.cryptotweets.feed.db.RemoteKeys
import app.cryptotweets.feed.models.Tweet
import app.cryptotweets.utils.CryptoTweetsDatabase
import app.cryptotweets.utils.FEED_LIST_ID
import app.cryptotweets.utils.FEED_LIST_SIZE
import app.cryptotweets.utils.FEED_LIST_TYPE
import app.cryptotweets.utils.PAGE_DEFAULT
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val service: FeedService,
    private val database: CryptoTweetsDatabase
) : RemoteMediator<Int, Tweet>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Tweet>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGE_DEFAULT
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null) PAGE_DEFAULT
                else if (remoteKeys.nextKey == null)
                    return MediatorResult.Success(endOfPaginationReached = true)
                else remoteKeys.nextKey
            }
        }

        try {
            val tweets = service.getTweets(
                listType = FEED_LIST_TYPE,
                listId = FEED_LIST_ID,
                count = FEED_LIST_SIZE,
                page = page.toString()
            )

            val endOfPaginationReached = tweets.isEmpty()
            database.withTransaction {
                // Clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.feedDao().clearTweets()
                }
                val prevKey = if (page == PAGE_DEFAULT) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = tweets.map {
                    RemoteKeys(tweetId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.feedDao().addTweets(tweets)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Tweet>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { tweet ->
                // Get the remote keys of the last item retrieved
                database.remoteKeysDao().remoteKeysTweetId(tweet.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Tweet>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { tweetId ->
                database.remoteKeysDao().remoteKeysTweetId(tweetId)
            }
        }
    }

}