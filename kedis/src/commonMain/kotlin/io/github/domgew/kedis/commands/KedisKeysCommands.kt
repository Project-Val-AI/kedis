package io.github.domgew.kedis.commands

import io.github.domgew.kedis.commands.keys.KeysCommand

public object KedisKeysCommands {

    /**
     * Returns the keys matching the given [pattern].
     *
     * [https://redis.io/commands/keys/](https://redis.io/commands/keys/)
     * @return The keys matching [pattern]
     */
    public fun keys(
        pattern: String,
    ): KedisCommand<List<String>> =
        KeysCommand(
            pattern = pattern,
        )
}
