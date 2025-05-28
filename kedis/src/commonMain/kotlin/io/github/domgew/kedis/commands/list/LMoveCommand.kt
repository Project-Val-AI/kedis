package io.github.domgew.kedis.commands.list

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.arguments.list.ListEnd
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.RedisMessage

// // see https://redis.io/commands/lmove/
internal class LMoveCommand(
    val sourceKey: String,
    val destinationKey: String,
    val sourceEnd: ListEnd,
    val destinationEnd: ListEnd,
) : KedisFullCommand<String?> {

    override fun fromRedisResponse(
        response: RedisMessage,
    ): String? =
        when (response) {
            is RedisMessage.NullMessage ->
                null

            is RedisMessage.BulkStringMessage ->
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
                RedisMessage.BulkStringMessage(sourceKey),
                RedisMessage.BulkStringMessage(destinationKey),
                RedisMessage.BulkStringMessage(
                    when (sourceEnd) {
                        ListEnd.HEAD ->
                            END_HEAD

                        ListEnd.TAIL ->
                            END_TAIL
                    },
                ),
                RedisMessage.BulkStringMessage(
                    when (destinationEnd) {
                        ListEnd.HEAD ->
                            END_HEAD

                        ListEnd.TAIL ->
                            END_TAIL
                    },
                ),
            ),
        )

    companion object {

        private const val OPERATION_NAME = "LMOVE"

        private const val END_HEAD = "LEFT"
        private const val END_TAIL = "RIGHT"
    }
}
