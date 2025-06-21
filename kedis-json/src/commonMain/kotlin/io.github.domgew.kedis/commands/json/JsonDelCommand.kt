package io.github.domgew.kedis.commands.json

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.JsonPath
import io.github.domgew.kedis.impl.RedisMessage

internal class JsonDelCommand(
    private val key: String,
    private val path: JsonPath = JsonPath.ROOT
): KedisFullCommand<Long> {

    override fun fromRedisResponse(response: RedisMessage): Long =
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

    override fun toRedisRequest(): RedisMessage {

        val value = arrayListOf<RedisMessage>()

        value.add(RedisMessage.BulkStringMessage(OPERATION_NAME))
        value.add(RedisMessage.BulkStringMessage(key))
        value.add(RedisMessage.BulkStringMessage(path.toString()))

        return RedisMessage.ArrayMessage(
            value = value
        )
    }

    companion object {

        const val OPERATION_NAME = "JSON.DEL"

    }

}
