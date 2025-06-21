package io.github.domgew.kedis.commands.json

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.arguments.value.SetOptions
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.JsonPath
import io.github.domgew.kedis.impl.RedisMessage
import io.github.domgew.kedis.results.value.SetResult
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

public class JsonSetCommand<T: Any>(
    private val key: String,
    private val value: T,
    private val serializer: KSerializer<T>,
    private val path: JsonPath,
    private val previousKeyHandling: SetOptions.PreviousKeyHandling?
) : KedisFullCommand<SetResult> {

    override fun fromRedisResponse(response: RedisMessage): SetResult =
        when {
            response is RedisMessage.NullMessage ->
                SetResult.Aborted

            response is RedisMessage.StringMessage
                && response.value == "OK" ->
                SetResult.Ok

            response is RedisMessage.StringMessage ->
                throw KedisException.WrongResponseException(
                    message = "Expected \"OK\" or data, was \"${response.value}\"",
                )

            response is RedisMessage.ErrorMessage ->
                handleRedisErrorResponse(
                    response = response,
                )

            else ->
                throw KedisException.WrongResponseException(
                    message = "Expected string response, was ${response::class.simpleName}",
                )
        }

    override fun toRedisRequest(): RedisMessage {

        val value = arrayListOf<RedisMessage>()

        value.addAll(listOf(
            RedisMessage.BulkStringMessage(OPERATION_NAME),
            RedisMessage.BulkStringMessage(key),
            RedisMessage.BulkStringMessage(path.toString()),
            RedisMessage.BulkStringMessage(Json.encodeToString(this.serializer, this.value)),
        ))

        previousKeyHandling?.apiValue?.also{
            value.add(RedisMessage.BulkStringMessage(previousKeyHandling.apiValue!!))

        }

        return RedisMessage.ArrayMessage(
            value = value
        )
    }

    public companion object {
        private const val OPERATION_NAME = "JSON.SET"
    }

}
