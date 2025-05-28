package io.github.domgew.kedis.arguments.list

public sealed class ListRemoveCount {

    /**
     * [n] items beginning from the head of the list.
     */
    public data class FromHead(
        val n: Int,
    ) : ListRemoveCount() {

        init {
            require(n > 0) {
                "[n] must be greater than 0"
            }
        }
    }

    /**
     * [n] items beginning from the tail of the list.
     */
    public data class FromTail(
        val n: Int,
    ) : ListRemoveCount() {

        init {
            require(n > 0) {
                "[n] must be greater than 0"
            }
        }
    }

    public data object All : ListRemoveCount()
}
