package io.github.domgew.kedis.results.value

// https://redis.io/commands/expiretime/
// https://redis.io/commands/pexpiretime/
/**
 * When the item expires.
 *
 * @see NotFound
 * @see Never
 * @see AtUnixSecond
 * @see AtUnixMillisecond
 */
public sealed interface ExpireTimeResult {

    public data object NotFound : ExpireTimeResult

    public data object Never : ExpireTimeResult

    public class AtUnixSecond internal constructor(
        public val seconds: Long,
    ) : ExpireTimeResult {

        override fun equals(
            other: Any?,
        ): Boolean {
            if (this === other)
                return true
            if (
                other == null
                || other !is AtUnixSecond
            )
                return false

            return seconds == other.seconds
        }

        override fun hashCode(): Int {
            return seconds.hashCode()
        }
    }

    public class AtUnixMillisecond internal constructor(
        public val milliseconds: Long,
    ) : ExpireTimeResult {

        override fun equals(
            other: Any?,
        ): Boolean {
            if (this === other)
                return true
            if (
                other == null
                || other !is AtUnixMillisecond
            )
                return false

            return milliseconds == other.milliseconds
        }

        override fun hashCode(): Int {
            return milliseconds.hashCode()
        }
    }
}
