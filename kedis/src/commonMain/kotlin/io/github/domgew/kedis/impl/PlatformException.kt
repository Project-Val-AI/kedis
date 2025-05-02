package io.github.domgew.kedis.impl

internal expect inline fun <T> commoniseConnectException(
    block: () -> T,
): T

internal expect inline fun <T> commoniseNetworkExceptions(
    block: () -> T,
): T
