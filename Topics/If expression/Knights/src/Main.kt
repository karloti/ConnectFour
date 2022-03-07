import java.util.*
import kotlin.math.hypot
import kotlin.math.sqrt

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    // write your code here
    val x1 = scanner.nextInt()
    val y1 = scanner.nextInt()
    val x2 = scanner.nextInt()
    val y2 = scanner.nextInt()

    // distance from two knights need a square root of 5
    print(if (hypot((x1 - x2).toFloat(), (y1 - y2).toFloat()) == sqrt(5.0F)) "YES" else "NO")
}
