import java.util.Scanner

fun swapInts(ints: IntArray): IntArray {
    return intArrayOf(ints[1], ints[0])
}

fun main() {
    val scanner = Scanner(System.`in`)
    while (scanner.hasNextLine()) {
        var ints = intArrayOf(
            scanner.nextLine().toInt(),
            scanner.nextLine().toInt(),
        )
        ints = swapInts(ints).also { result ->
            check(ints[0] == result[1]) { "The number with index 0 is not swapped!" }
            check(ints[1] == result[0]) { "The number with index 1 is not swapped!" }
        }
        println(ints[0])
        println(ints[1])
    }
}