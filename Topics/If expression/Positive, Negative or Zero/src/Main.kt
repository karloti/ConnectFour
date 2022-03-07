import kotlin.math.sign
/**
 * It is not superior to the other ways, just to be different
 *
 * Int.sign return -1, 0 , 1
 * */

fun main() {
    val result = listOf("negative", "zero", "positive")
    val index = readLine()!!.toInt().sign + 1
    println(result[index])
}