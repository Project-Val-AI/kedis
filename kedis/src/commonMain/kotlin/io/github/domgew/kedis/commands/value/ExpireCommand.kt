package io.github.domgew.kedis.commands.value

import io.github.domgew.kedis.KedisException
import io.github.domgew.kedis.arguments.value.ExpireOption
import io.github.domgew.kedis.commands.KedisFullCommand
import io.github.domgew.kedis.impl.RedisMessage
import io.github.domgew.kedis.results.value.ExpireTimeResult

internal class ExpireCommand(
    private val key: String,
    private val seconds: Long,
    private val option: ExpireOption = ExpireOption.OVERRIDE
) : KedisFullCommand<ExpireTimeResult> {

    override fun fromRedisResponse(response: RedisMessage): ExpireTimeResult =
        when (response) {
            is RedisMessage.IntegerMessage ->
                when (response.value) {
                    -1L ->
                        ExpireTimeResult.Never

                    -2L ->
                        ExpireTimeResult.NotFound

                    else ->
                        ExpireTimeResult.AtUnixSecond(
                        seconds = response.value,
                    )
                }

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

        value.addAll(listOf(
            RedisMessage.BulkStringMessage(OPERATION_NAME),
            RedisMessage.BulkStringMessage(key),
            RedisMessage.BulkStringMessage(seconds.toString()),
        ))

        option.apiValue?.also{value.add(RedisMessage.BulkStringMessage(it))}

        return RedisMessage.ArrayMessage(
            value = value
        )
    }
    companion object {
        internal const val OPERATION_NAME = "EXPIRE"
    }
}
