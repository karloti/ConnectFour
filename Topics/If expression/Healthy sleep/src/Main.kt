fun main() = List(3) { readln().toInt() }
    .let { (a, b, h) ->
        when {
            h < a -> "Deficiency"
            b < h -> "Excess"
            else -> "Normal"
        }
    }.let(::println)
