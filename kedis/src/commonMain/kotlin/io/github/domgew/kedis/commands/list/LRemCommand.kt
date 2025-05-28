package io.github.domgew.kedis.commands.list

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.arguments.list.ListRemoveCount
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.RedisMessage

// // see https://redis.io/commands/lrem/
internal class LRemCommand(
    val key: String,
    val count: ListRemoveCount,
    val value: String,
) : KedisFullCommand<Long> {

    override fun fromRedisResponse(
        response: RedisMessage,
    ): Long =
        when (response) {
            is RedisMessage.IntegerMessage ->
                response.value

            is RedisMessage.ErrorMessage ->
                handleRedisErrorResponse(
                    response = response,
                )

            else ->
                throw KedisException.WrongResponseException(
                    message = "Expected integer response, was ${response::class.simpleName}",
                )
        }

    override fun toRedisRequest(): RedisMessage =
        RedisMessage.ArrayMessage(
            value = listOf(
                RedisMessage.BulkStringMessage(OPERATION_NAME),
                RedisMessage.BulkStringMessage(key),
                RedisMessage.BulkStringMessage(
                    when (count) {
                        ListRemoveCount.All ->
                            0

                        is ListRemoveCount.FromHead ->
                            count.n

                        is ListRemoveCount.FromTail ->
                            count.n * -1
                    }
                        .toString(),
                ),
                RedisMessage.BulkStringMessage(value),
            ),
        )

    companion object {
        private const val OPERATION_NAME = "LREM"
    }
}
