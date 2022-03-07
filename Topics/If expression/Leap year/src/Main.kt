fun main() {
    val n = readLine()!!.toInt()
    if (n in 1900..3000)
        if (n % 4 == 0)
            if (n % 100 == 0)
                if (n % 400 == 0) println("Leap")
                else println("Regular")
            else println("Leap")
        else println("Regular")
    else println("N not in range 1900..3000")
}