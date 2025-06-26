package io.github.domgew.kedis.commands.json

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.JsonPath
import io.github.domgew.kedis.impl.RedisMessage

internal class JsonToggleCommand(
    private val key: String,
    private val path: JsonPath,
) : KedisFullCommand<Array<Long>> {

    override fun fromRedisResponse(response: RedisMessage): Array<Long> =
        when (response) {
            is RedisMessage.ArrayMessage -> response.value
                .map{it as RedisMessage.IntegerMessage}
                .map{it.value}
                .toTypedArray()

            is RedisMessage.ErrorMessage ->
                handleRedisErrorResponse(
                    response = response,
                )

            else ->
                throw KedisException.WrongResponseException(
                    message = "Expected integer response, was ${response::class.simpleName}",
                )
        }

    override fun toRedisRequest(): RedisMessage {
        return RedisMessage.ArrayMessage(
            value = listOf(
                RedisMessage.BulkStringMessage(OPERATION_NAME),
                RedisMessage.BulkStringMessage(key),
                RedisMessage.BulkStringMessage(path.toString())
            )
        )
    }

    public companion object {
        private const val OPERATION_NAME = "JSON.TOGGLE"
    }
}
