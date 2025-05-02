package io.github.domgew.kedis.commands

import io.github.domgew.kedis.commands.KedisHashCommands.hashGet
import io.github.domgew.kedis.commands.KedisHashCommands.hashGetAll
import io.github.domgew.kedis.commands.KedisHashCommands.hashGetAllBinary
import io.github.domgew.kedis.commands.KedisHashCommands.hashGetBinary
import io.github.domgew.kedis.commands.KedisHashCommands.hashSet
import io.github.domgew.kedis.commands.KedisHashCommands.hashSetBinary
import io.github.domgew.kedis.commands.hash.HashDelCommand
import io.github.domgew.kedis.commands.hash.HashExistsCommand
import io.github.domgew.kedis.commands.hash.HashGetAllBinaryCommand
import io.github.domgew.kedis.commands.hash.HashGetAllCommand
import io.github.domgew.kedis.commands.hash.HashGetBinaryCommand
import io.github.domgew.kedis.commands.hash.HashGetCommand
import io.github.domgew.kedis.commands.hash.HashKeysCommand
import io.github.domgew.kedis.commands.hash.HashLengthCommand
import io.github.domgew.kedis.commands.hash.HashSetBinaryCommand
import io.github.domgew.kedis.commands.hash.HashSetCommand

public object KedisHashCommands {

    /**
     * Removes the provided [field]s from the hash map behind [key]. If a [field] does not exist, no error is thrown.
     *
     * [https://redis.io/commands/hdel/](https://redis.io/commands/hdel/)
     * @return The number of removed provided [field]s
     */
    public fun hashDel(
        key: String,
        vararg field: String,
    ): KedisCommand<Long> =
        HashDelCommand(
            key = key,
            fields = field.asList(),
        )

    /**
     * Checks whether the given [field] exists on the hash map behind [key].
     *
     * [https://redis.io/commands/hexists/](https://redis.io/commands/hexists/)
     * @return The number of provided [key]s that do exist
     */
    public fun hashExists(
        key: String,
        field: String,
    ): KedisCommand<Boolean> =
        HashExistsCommand(
            key = key,
            field = field,
        )

    /**
     * Gets the value behind the given [field] in the [key] hash map.
     *
     * [https://redis.io/commands/hget/](https://redis.io/commands/hget/)
     * @return The value or NULL
     * @see hashGetBinary
     */
    public fun hashGet(
        key: String,
        field: String,
    ): KedisCommand<String?> =
        HashGetCommand(
            key = key,
            field = field,
        )

    /**
     * Gets the hash map behind the given [key].
     *
     * [https://redis.io/commands/hgetall/](https://redis.io/commands/hgetall/)
     * @return The map or NULL
     * @see hashGetAllBinary
     */
    public fun hashGetAll(
        key: String,
    ): KedisCommand<Map<String, String>?> =
        HashGetAllCommand(
            key = key,
        )

    /**
     * Gets the hash map behind the given [key].
     *
     * [https://redis.io/commands/hgetall/](https://redis.io/commands/hgetall/)
     * @return The map or NULL
     * @see hashGetAll
     */
    public fun hashGetAllBinary(
        key: String,
    ): KedisCommand<Map<String, ByteArray>?> =
        HashGetAllBinaryCommand(
            key = key,
        )

    /**
     * Gets the value behind the given [field] in the [key] hash map.
     *
     * [https://redis.io/commands/hget/](https://redis.io/commands/hget/)
     * @return The value or NULL
     * @see hashGet
     */
    public fun hashGetBinary(
        key: String,
        field: String,
    ): KedisCommand<ByteArray?> =
        HashGetBinaryCommand(
            key = key,
            field = field,
        )

    /**
     * Gets the fields of the hash map behind [key].
     *
     * [https://redis.io/commands/hkeys/](https://redis.io/commands/hkeys/)
     * @return The field names
     */
    public fun hashKeys(
        key: String,
    ): KedisCommand<List<String>?> =
        HashKeysCommand(
            key = key,
        )

    /**
     * Gets the number of fields of the hash map behind [key].
     *
     * [https://redis.io/commands/hlen/](https://redis.io/commands/hlen/)
     * @return The number of fields
     */
    public fun hashLength(
        key: String,
    ): KedisCommand<Long> =
        HashLengthCommand(
            key = key,
        )

    /**
     * Sets the given [fieldValues] on the hash map behind [key]. If the hash map does not exist, it is created.
     *
     * If the hash map already contains other field than those provided in [fieldValues], they are not removed. If the field is already present, it is overwritten.
     *
     * [https://redis.io/commands/hset/](https://redis.io/commands/hset/)
     * @return The number of fields that were added (not just set)
     * @see hashSetBinary
     */
    public fun hashSet(
        key: String,
        fieldValues: Map<String, String>,
    ): KedisCommand<Long> =
        HashSetCommand(
            key = key,
            fieldValues = fieldValues,
        )

    /**
     * Sets the given [fieldValues] on the hash map behind [key]. If the hash map does not exist, it is created.
     *
     * If the hash map already contains other field than those provided in [fieldValues], they are not removed. If the field is already present, it is overwritten.
     *
     * [https://redis.io/commands/hset/](https://redis.io/commands/hset/)
     * @return The number of fields that were added (not just set)
     * @see hashSet
     */
    public fun hashSetBinary(
        key: String,
        fieldValues: Map<String, ByteArray>,
    ): KedisCommand<Long> =
        HashSetBinaryCommand(
            key = key,
            fieldValues = fieldValues,
        )
}
