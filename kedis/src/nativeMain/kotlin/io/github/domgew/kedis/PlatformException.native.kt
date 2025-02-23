package io.github.domgew.kedis

import io.ktor.utils.io.errors.PosixException
import kotlinx.io.EOFException
import kotlinx.io.IOException

internal actual suspend fun <T> commoniseConnectException(
    block: suspend () -> T,
): T =
    try {
        commoniseNetworkExceptions(
            block = block,
        )
    } catch (ex: IllegalStateException) {
        if (
            ex.message?.contains("Failed", ignoreCase = true) != true
            || ex.message?.contains("connect", ignoreCase = true) != true
        ) {
            throw ex
        }

        throw KedisException.ConnectException(
            cause = ex,
        )
    }

internal actual suspend fun <T> commoniseNetworkExceptions(
    block: suspend () -> T,
): T =
    try {
        block()
    } catch (ex: EOFException) {
        throw KedisException.GenericNetworkException(
            cause = ex,
        )
    } catch (ex: IOException) {
        throw KedisException.GenericNetworkException(
            cause = ex,
        )
    } catch (ex: PosixException) {
        throw KedisException.GenericNetworkException(
            cause = ex,
        )
    }
