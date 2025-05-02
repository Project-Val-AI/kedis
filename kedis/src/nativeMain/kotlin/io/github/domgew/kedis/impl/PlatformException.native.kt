package io.github.domgew.kedis.impl

import io.github.domgew.kedis.KedisException
import io.ktor.utils.io.errors.PosixException
import kotlinx.io.EOFException
import kotlinx.io.IOException

internal actual inline fun <T> commoniseConnectException(
    block: () -> T,
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

internal actual inline fun <T> commoniseNetworkExceptions(
    block: () -> T,
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
