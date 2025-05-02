package io.github.domgew.kedis.impl

import io.github.domgew.kedis.KedisException
import java.net.ConnectException

internal actual inline fun <T> commoniseConnectException(
    block: () -> T,
): T =
    try {
        commoniseNetworkExceptions(
            block = block,
        )
    } catch (ex: ConnectException) {
        throw KedisException.ConnectException(
            cause = ex,
        )
    }

internal actual inline fun <T> commoniseNetworkExceptions(
    block: () -> T,
): T =
    try {
        block()
    } catch (ex: kotlinx.coroutines.channels.ClosedReceiveChannelException) {
        throw KedisException.GenericNetworkException(
            cause = ex,
        )
    } catch (ex: java.io.IOException) {
        throw KedisException.GenericNetworkException(
            cause = ex,
        )
    }
