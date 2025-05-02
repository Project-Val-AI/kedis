package io.github.domgew.kedis.results.value

// https://redis.io/commands/ttl/
// https://redis.io/commands/pttl/
/**
 * How long the item still has to live.
 *
 * @see NotFound
 * @see Never
 * @see InSeconds
 * @see InMilliseconds
 */
public sealed interface TtlResult {

    public data object NotFound : TtlResult

    public data object Never : TtlResult

    public class InSeconds internal constructor(
        public val seconds: Long,
    ) : TtlResult {

        override fun equals(
            other: Any?,
        ): Boolean {
            if (this === other)
                return true
            if (
                other == null
                || other !is InSeconds
            )
                return false

            return seconds == other.seconds
        }

        override fun hashCode(): Int {
            return seconds.hashCode()
        }
    }

    public class InMilliseconds internal constructor(
        public val milliseconds: Long,
    ) : TtlResult {

        override fun equals(
            other: Any?,
        ): Boolean {
            if (this === other)
                return true
            if (
                other == null
                || other !is InMilliseconds
            )
                return false

            return milliseconds == other.milliseconds
        }

        override fun hashCode(): Int {
            return milliseconds.hashCode()
        }
    }
}
