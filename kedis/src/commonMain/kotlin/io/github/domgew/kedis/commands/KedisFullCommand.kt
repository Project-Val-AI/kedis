package io.github.domgew.kedis.commands

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.impl.RedisMessage

public interface KedisFullCommand<out T> : KedisCommand<T> {

    public fun toRedisRequest(): RedisMessage

    public fun fromRedisResponse(
        response: RedisMessage,
    ): T

    public fun handleRedisErrorResponse(
        response: RedisMessage.ErrorMessage,
    ): Nothing {
        throw KedisException.RedisErrorResponseException(
            message = response.value,
        )
    }
}
