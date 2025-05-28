package io.github.domgew.kedis.commands.list

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.RedisMessage

// // see https://redis.io/commands/lrange/
internal class LRangeCommand(
    val key: String,
    val start: Int,
    val end: Int,
) : KedisFullCommand<List<String>> {

    override fun fromRedisResponse(
        response: RedisMessage,
    ): List<String> =
        when (response) {
            is RedisMessage.ArrayMessage ->
                response.value
                    .map { item ->
                        when (item) {
                            is RedisMessage.BulkStringMessage ->
                                item.value

                            else ->
                                throw KedisException.WrongResponseException(
                                    message = "Expected string item, was ${item::class.simpleName}",
                                )
                        }
                    }

            is RedisMessage.ErrorMessage ->
                handleRedisErrorResponse(
                    response = response,
                )

            else ->
                throw KedisException.WrongResponseException(
                    message = "Expected array response, was ${response::class.simpleName}",
                )
        }

    override fun toRedisRequest(): RedisMessage =
        RedisMessage.ArrayMessage(
            value = listOf(
                RedisMessage.BulkStringMessage(OPERATION_NAME),
                RedisMessage.BulkStringMessage(key),
                RedisMessage.BulkStringMessage(start.toString()),
                RedisMessage.BulkStringMessage(end.toString()),
            ),
        )

    companion object {
        private const val OPERATION_NAME = "LRANGE"
    }
}
