package app.cryptotweets.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform


/**
 * Returns a flow which performs the given [action] on each value of the original flow's [Event].
 */
public fun <T> Flow<Event<T?>>.onEachEvent(action: suspend (T) -> Unit): Flow<T> = transform { value ->
    value.getContentIfNotHandled()?.let {
        action(it)
        return@transform emit(it)
    }
}
