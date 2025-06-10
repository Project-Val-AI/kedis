package io.github.domgew.kedis.commands.json

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.JsonPath
import io.github.domgew.kedis.impl.RedisMessage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

public class JsonGetCommand<T: Any>(
    private val key: String,
    private val serializer: KSerializer<T>,
    private val path: JsonPath = JsonPath.ROOT
) : KedisFullCommand<T?> {

    override fun fromRedisResponse(response: RedisMessage): T? =
        when (response) {
            is RedisMessage.BulkStringMessage ->
                Json.decodeFromString(this.serializer,response.value)

            is RedisMessage.NullMessage ->
                null

            is RedisMessage.ErrorMessage ->
                handleRedisErrorResponse(
                    response = response,
                )

            else ->
                throw KedisException.WrongResponseException(
                    message = "Expected string or null response, was ${response::class.simpleName}",
                )
        }

    override fun toRedisRequest(): RedisMessage =
        RedisMessage.ArrayMessage(
            value = listOf(
                RedisMessage.BulkStringMessage(OPERATION_NAME),
                RedisMessage.BulkStringMessage(key),
                RedisMessage.BulkStringMessage(path.toString())
            ),
        )

    public companion object {
        private const val OPERATION_NAME = "JSON.GET"
    }

}
