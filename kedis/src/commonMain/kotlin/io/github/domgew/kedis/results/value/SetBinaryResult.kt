package io.github.domgew.kedis.results.value

// https://redis.io/commands/set/
public sealed interface SetBinaryResult {

    public val successful: Boolean
    public val written: Boolean

    public data object Aborted : SetBinaryResult {

        override val successful: Boolean = true
        override val written: Boolean = false
    }

    public data object Ok : SetBinaryResult {

        override val successful: Boolean = true
        override val written: Boolean = true
    }

    public data object NotFound : SetBinaryResult {

        override val successful: Boolean = true
        override val written: Boolean = true
    }

    public class PreviousValue internal constructor(
        public val data: ByteArray,
    ) : SetBinaryResult {

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

            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int =
            data.contentHashCode()
    }
}
