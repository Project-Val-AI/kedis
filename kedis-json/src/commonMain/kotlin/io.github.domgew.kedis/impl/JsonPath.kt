package io.github.domgew.kedis.impl

import kotlin.reflect.KProperty

public data class JsonPath(
    private var value: String
) {

    public constructor(property: KProperty<*>):this(path(property))

    public operator fun plus(path: JsonPath):JsonPath {
        this.value += path.value
        return this
    }

    public operator fun plus(property: KProperty<*>):JsonPath {
        this.value += path(property)
        return this
    }

    public fun isRoot():Boolean {
        return this == ROOT
    }

    override fun toString(): String {
        return this.value
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonPath && this.value == other.value
    }

    public companion object {

        public val ROOT: JsonPath = JsonPath("$")
        public val LEGACY_ROOT: JsonPath = JsonPath(".")

        private fun path(property: KProperty<*>):String {
            return ".${property.name}"
        }

    }

}
