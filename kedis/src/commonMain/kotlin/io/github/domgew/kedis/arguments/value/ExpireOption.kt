package io.github.domgew.kedis.arguments.value

public enum class ExpireOption(
    public val apiValue: String?,
) {

    /**
     * Set expiry to the given value in any case.
     */
    OVERRIDE(null),

    /**
     * Set expiry only when the key has no expiry.
     */
    KEEP_IF_EXISTS("NX"),

    /**
     * Set expiry only when the key has an existing expiry.
     */
    OVERRIDE_ONLY("XX"),

    /**
     * Set expiry only when the new expiry is greater than current one.
     */
    GREATER_THAN_ONLY("GT"),

    /**
     * Set expiry only when the new expiry is less than current one.
     */
    LOWER_THAN_ONLY("LT"),
}
