package io.github.domgew.kedis.commands

import io.github.domgew.kedis.arguments.value.SetOptions
import io.github.domgew.kedis.commands.json.JsonDelCommand
import io.github.domgew.kedis.commands.json.JsonGetCommand
import io.github.domgew.kedis.commands.json.JsonSetCommand
import io.github.domgew.kedis.commands.json.JsonTypeCommand
import io.github.domgew.kedis.impl.JsonPath
import io.github.domgew.kedis.results.value.SetResult
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

@OptIn(InternalSerializationApi::class)
public object KedisJsonCommands {

    /**
     * Gets the value behind the given [key].
     *
     * [https://redis.io/commands/json.get/](https://redis.io/commands/json.get/)
     * @return The value or NULL
     */
    public inline fun <reified T: Any> jsonGet(
        key: String,
        serializer: KSerializer<T> = T::class.serializer(),
        path: JsonPath = JsonPath.ROOT,
    ): KedisCommand<T?> {
        return JsonGetCommand(
            key = key,
            serializer = serializer,
            path = path,
        )
    }

    /**
     * Sets the value behind the given [key], minding the [options].
     *
     * [https://redis.io/commands/json.set/](https://redis.io/commands/json.set/)
     * @return Whether the operation was successful and the previous value if requested
     */
    public inline fun <reified T: Any> jsonSet(
        key: String,
        value: T,
        serializer: KSerializer<T> = T::class.serializer(),
        path: JsonPath = JsonPath.ROOT,
        previousKeyHandling: SetOptions.PreviousKeyHandling? = null
    ): KedisCommand<SetResult> =
        JsonSetCommand(
            key = key,
            value = value,
            serializer = serializer,
            path = path,
            previousKeyHandling = previousKeyHandling
        )

    /**
     * Delete a value.
     *
     * [https://redis.io/commands/json.del/](https://redis.io/commands/json.del/)
     * @return number of paths deleted
     */
    public fun jsonDel(
        key: String,
        path: JsonPath = JsonPath.ROOT
    ): KedisCommand<Long> =
        JsonDelCommand(
            key = key,
            path = path,
        )

    /**
     * Report the type of JSON value at path
     *
     * [https://redis.io/commands/json.type/](https://redis.io/commands/json.type/)
     * @return an array of string replies for each path, specified as the value's type.
     */
    public fun jsonType(
        key: String,
        path: JsonPath = JsonPath.ROOT
    ): KedisCommand<Array<String>> {
        return JsonTypeCommand(
            key = key,
            path = path
        )
    }
}
