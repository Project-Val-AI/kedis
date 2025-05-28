package io.github.domgew.kedis.commands

import io.github.domgew.kedis.arguments.list.ListEnd
import io.github.domgew.kedis.arguments.list.ListRemoveCount
import io.github.domgew.kedis.commands.list.LLenCommand
import io.github.domgew.kedis.commands.list.LMoveCommand
import io.github.domgew.kedis.commands.list.LPopCommand
import io.github.domgew.kedis.commands.list.LPushCommand
import io.github.domgew.kedis.commands.list.LRangeCommand
import io.github.domgew.kedis.commands.list.LRemCommand
import io.github.domgew.kedis.commands.list.RPopCommand

public object KedisListCommands {

    /**
     * Returns the length of the list identified by [key].
     *
     * [https://redis.io/commands/llen/](https://redis.io/commands/llen/)
     * @return The list's length
     */
    public fun llen(
        key: String,
    ): KedisCommand<Long> =
        LLenCommand(
            key = key,
        )

    /**
     * Moves the first element from the [sourceEnd] of the list identified by [sourceKey] to the first position from the [destinationEnd] of the list identified by [destinationKey].
     *
     * For list at "l1" being ("a", "b", "c") and list at "l2" being ("x", "y", "z"), lmove("l1", "l2", TAIL, HEAD) would result in "l1" being ("a", "b") and "l2" being ("c", "x", "y", "z").
     *
     * [https://redis.io/commands/lmove/](https://redis.io/commands/lmove/)
     * @return The moved element or NULL, if the source was empty
     */
    public fun lmove(
        sourceKey: String,
        destinationKey: String,
        sourceEnd: ListEnd,
        destinationEnd: ListEnd,
    ): KedisCommand<String?> =
        LMoveCommand(
            sourceKey = sourceKey,
            destinationKey = destinationKey,
            sourceEnd = sourceEnd,
            destinationEnd = destinationEnd,
        )

    /**
     * Pops up to [n] elements from the head of the list identified by [key].
     *
     * [https://redis.io/commands/lpop/](https://redis.io/commands/lpop/)
     * @return The values or NULL
     */
    public fun lpop(
        key: String,
        n: Int = 1,
    ): KedisCommand<List<String>?> =
        LPopCommand(
            key = key,
            n = n,
        )

    /**
     * Pushes [values] to the head of the list identified by [key].
     *
     * [https://redis.io/commands/lpush/](https://redis.io/commands/lpush/)
     * @return The list's length after insertion
     */
    public fun lpush(
        key: String,
        values: List<String>,
    ): KedisCommand<Long> {
        require(values.isNotEmpty()) {
            "[values] must not be empty"
        }

        return LPushCommand(
            key = key,
            values = values,
        )
    }

    /**
     * Gets the element from [start] index (zero-based - first item is 0) to [end] index (zero-based inclusive) from the list identified by [key].
     *
     * Negative indices (counting from the end - -1 is the last element) are allowed for [start] and [end].
     *
     * * [0, -1] would be the whole list
     * * [-2, -1] would be the last two items
     * * [0, 2] would be the first two items
     *
     * [https://redis.io/commands/lrane/](https://redis.io/commands/lrane/)
     * @return The values within the range or an empty list
     */
    public fun lrange(
        key: String,
        start: Int,
        end: Int,
    ): KedisCommand<List<String>> =
        LRangeCommand(
            key = key,
            start = start,
            end = end,
        )

    /**
     * Removed [count] elements matching [value] from the list identified by [key].
     *
     * [https://redis.io/commands/lrem/](https://redis.io/commands/lrem/)
     * @return The number of elements that were removed
     */
    public fun lrem(
        key: String,
        count: ListRemoveCount,
        value: String,
    ): KedisCommand<Long> =
        LRemCommand(
            key = key,
            count = count,
            value = value,
        )

    /**
     * Pops up to [n] elements from the tail of the list identified by [key].
     *
     * [https://redis.io/commands/rpop/](https://redis.io/commands/rpop/)
     * @return The values or NULL
     */
    public fun rpop(
        key: String,
        n: Int = 1,
    ): KedisCommand<List<String>?> =
        RPopCommand(
            key = key,
            n = n,
        )
}
