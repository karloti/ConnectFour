fun String?.capitalize() = when {
    isNullOrBlank() -> this
    length == 1 -> uppercase()
    else -> this[0].uppercase() + substring(1)
}.also { // Logging
    println("Before: $this")
    println("After: $it")
}