package io.github.domgew.kedis.commands

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.impl.RedisMessage

internal interface KedisFullCommand<out T> : KedisCommand<T> {

    fun toRedisRequest(): RedisMessage

    fun fromRedisResponse(
        response: RedisMessage,
    ): T

    fun handleRedisErrorResponse(
        response: RedisMessage.ErrorMessage,
    ): Nothing {
        throw KedisException.RedisErrorResponseException(
            message = response.value,
        )
    }
}
