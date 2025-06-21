package io.github.domgew.kedis.commands.json

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.JsonPath
import io.github.domgew.kedis.impl.RedisMessage

internal class JsonTypeCommand(
    private val key: String,
    private val path: JsonPath,
) : KedisFullCommand<Array<String>> {

    override fun fromRedisResponse(response: RedisMessage): Array<String> {
        return when (response) {
            is RedisMessage.ArrayMessage -> response.value
                .map{it as RedisMessage.StringMessage}
                .map{it.value}
                .toTypedArray()

            is RedisMessage.NullMessage ->
                emptyArray()

            is RedisMessage.ErrorMessage ->
                handleRedisErrorResponse(
                    response = response,
                )

            else ->
                throw KedisException.WrongResponseException(
                    message = "Expected array, map, or null response, was ${response::class.simpleName}",
                )
        }
    }

    override fun toRedisRequest(): RedisMessage {
        return RedisMessage.ArrayMessage(
            value = listOf(
                RedisMessage.BulkStringMessage(key),
                RedisMessage.BulkStringMessage(path.toString())
            )
        )
    }

}
