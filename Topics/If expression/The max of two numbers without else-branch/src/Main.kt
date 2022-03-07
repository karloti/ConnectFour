import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    val a = scanner.nextInt()
    val b = scanner.nextInt()

//    @Suppress("INTEGER_OVERFLOW")
//    if ((a == Int.MAX_VALUE && b == Int.MIN_VALUE) || (a == Int.MIN_VALUE && b == Int.MAX_VALUE)) 
//    println(Int.MAX_VALUE) else

      println(a - (a - b and (a - b).shr(Int.SIZE_BITS - 1)))

}
