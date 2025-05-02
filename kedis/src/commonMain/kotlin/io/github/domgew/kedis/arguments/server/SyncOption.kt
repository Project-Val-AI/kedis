package io.github.domgew.kedis.arguments.server

public enum class SyncOption {
    SYNC,
    ASYNC,
    ;

    override fun toString(): String =
        this.name
}
