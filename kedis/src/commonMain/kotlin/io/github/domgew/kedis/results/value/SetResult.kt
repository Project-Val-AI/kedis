package io.github.domgew.kedis.results.value

// https://redis.io/commands/set/
public sealed interface SetResult {

    public val successful: Boolean
    public val written: Boolean

    public data object Aborted : SetResult {

        override val successful: Boolean = true
        override val written: Boolean = false
    }

    public data object Ok : SetResult {

        override val successful: Boolean = true
        override val written: Boolean = true
    }

    public data object NotFound : SetResult {

        override val successful: Boolean = true
        override val written: Boolean = true
    }

    public class PreviousValue internal constructor(
        public val value: String,
    ) : SetResult {

        override val successful: Boolean = true
        override val written: Boolean = true

        override fun equals(
            other: Any?,
        ): Boolean {
            if (this === other)
                return true
            if (
                other == null
                || other !is PreviousValue
            )
                return false

            if (successful != other.successful)
                return false
            if (written != other.written)
                return false
            if (value != other.value)
                return false

            return true
        }

        override fun hashCode(): Int {
            var result = successful.hashCode()
            result = 31 * result + written.hashCode()
            result = 31 * result + value.hashCode()
            return result
        }
    }
}
