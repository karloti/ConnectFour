import kotlin.math.abs

fun main() {
    val (x1, y1, x2, y2) = readLine()!!.split(" ").map { it.toInt() }
    println(if (abs(x1 - x2) == abs(y1 - y2) || x1 == x2 || y1 == y2) "YES" else "NO")
}
