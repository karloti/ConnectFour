package connectfour

import connectfour.ConnectFour.Disk
import connectfour.ConnectFour.ResultOfGame.*
import kotlin.math.abs

typealias Board = List<List<Disk>>

class ConnectFour {
    private val players: List<Player>
    private var activePlayer: Player
    private val rows: Int
    private val cols: Int
    private val board: Board
    private val numberOfGames: Int
    private var gamesCounter: Int

    sealed interface ResultOfDimension
    data class SuccessOfDimension(val rows: Int, val cols: Int) : ResultOfDimension
    sealed class ErrorOfDimension(val message: String) : ResultOfDimension
    object InvalidInput : ErrorOfDimension("Invalid input")
    object RowsOutOfRange : ErrorOfDimension("Board rows should be from 5 to 9")
    object ColumnsOutOfRange : ErrorOfDimension("Board columns should be from 5 to 9")

    sealed interface ResultOfColumnInput
    data class SuccessOfColumnInput(val col: Int) : ResultOfColumnInput
    sealed interface ErrorOfColumnInput : ResultOfColumnInput
    object ColumnIsOutOfRange : ErrorOfColumnInput
    object IncorrectColumnNumber : ErrorOfColumnInput
    object ColumnNumberIsFull : ErrorOfColumnInput
    object GameExit : ErrorOfColumnInput

    enum class ResultOfGame { WINNER, DRAW, NO_WINNER }
    enum class Color(val symbol: Char) { RED('o'), YELLOW('*'), EMPTY(' '), }
    data class Disk(var color: Color)
    data class Player(val name: String, val color: Color, var score: Int)
    data class Direction(val stepRow: Int, val stepCol: Int)

    private fun Player.next() = players.indexOf(this).inc().mod(players.size).let { players[it] }

    private fun Board.indexOfLastOccupiedRow(col: Int) = map { it[col] }.takeWhile { it.color != Color.EMPTY }.lastIndex

    private fun Board.clear() = flatten().forEach { it.color = Color.EMPTY }

    private fun Board.asString() = buildString {
        appendLine((1..cols).joinToString(" ", " "))
        repeat(rows) { appendLine("║" + "%s║".repeat(cols)) }
        appendLine("╚" + "═╩".repeat(cols - 1) + "═╝")
    }.format(*reversed().flatten().map { it.color.symbol }.toTypedArray())

    private fun choiceNumberOfGames(): Int {
        while (true) {
            println("Do you want to play single or multiple games?")
            println("For a single game, input 1 or press Enter")
            println("Input a number of games:")
            val choice = readln()
            when {
                choice.isEmpty() -> return 1
                choice.toIntOrNull()?.let { it > 0 } ?: false -> return choice.toInt()
                else -> println("Invalid input")
            }
        }
    }

    private fun swapActivePlayer() { activePlayer = activePlayer.next() }

    private fun readDimensions(choice: String = ""): ResultOfDimension {
        if (choice.isEmpty()) return SuccessOfDimension(6, 7)
        val result: MatchResult? = Regex("\\s*(\\d+)\\s*[xX]\\s*(\\d+)\\s*").matchEntire(choice)
        val (rows, cols) = result?.destructured?.toList()?.map(String::toInt) ?: return InvalidInput
        return when {
            rows !in 5..9 -> RowsOutOfRange
            cols !in 5..9 -> ColumnsOutOfRange
            else -> SuccessOfDimension(rows, cols)
        }
    }

    private fun readColumn(choice: String): ResultOfColumnInput {
        ((((choice
            .takeIf { !it.equals("end", true) } ?: return GameExit)
            .toIntOrNull() ?: return IncorrectColumnNumber)
            .takeIf { it in 1..cols } ?: return ColumnIsOutOfRange)
            .takeIf { board.indexOfLastOccupiedRow(it - 1) < rows - 1 } ?: return ColumnNumberIsFull)
            .let { return SuccessOfColumnInput(it - 1) }
    }

    private fun borderWithDirection(row_: Int, col_: Int, dir: Direction, color: Color): Pair<Int, Int> {
        var row = row_
        var col = col_
        while (true) {
            val newRow = row + dir.stepRow
            val newCol = col + dir.stepCol
            if (newRow in 0 until rows && newCol in 0 until cols && board[newRow][newCol].color == color) {
                row = newRow
                col = newCol
            } else break
        }
        return Pair(row, col)
    }

    private fun checkBoardForDraw(): ResultOfGame {
        for (col in 0 until cols) if (readColumn(col.inc().toString()) != ColumnNumberIsFull) return NO_WINNER
        return DRAW
    }

    private fun checkGameResult(row: Int, col: Int): ResultOfGame = listOf(
        Direction(stepRow = 0, stepCol = -1) to Direction(stepRow = 0, stepCol = 1), // horizontal
        Direction(stepRow = -1, stepCol = 0) to Direction(stepRow = 1, stepCol = 0), // vertical
        Direction(stepRow = -1, stepCol = -1) to Direction(stepRow = 1, stepCol = 1), // diagonal1
        Direction(stepRow = 1, stepCol = -1) to Direction(stepRow = -1, stepCol = 1), // diagonal2
    ).maxOf { (dir1: Direction, dir2: Direction) ->
        val (row1, col1) = borderWithDirection(row, col, dir1, activePlayer.color)
        val (row2, col2) = borderWithDirection(row, col, dir2, activePlayer.color)
        maxOf(abs(row1 - row2) + 1, abs(col1 - col2) + 1) // Distance of borders
    }.let { maxDistance -> if (maxDistance > 3) WINNER else checkBoardForDraw() }

    private fun play(): ResultOfGame {
        while (true) {
            println("${activePlayer.name}'s turn:")
            val choice = readln()
            when (val result: ResultOfColumnInput = readColumn(choice)) {
                IncorrectColumnNumber -> println("Incorrect column number")
                ColumnIsOutOfRange -> println("The column number is out of range (1 - $rows)")
                ColumnNumberIsFull -> println("Column $choice is full")
                GameExit -> return NO_WINNER
                is SuccessOfColumnInput -> board.indexOfLastOccupiedRow(result.col).let { row ->
                    board[row + 1][result.col].color = activePlayer.color
                    println(board.asString())
                    when (val resultOfGame = checkGameResult(row = row + 1, col = result.col)) {
                        WINNER -> {
                            activePlayer.score += 2
                            println("Player ${activePlayer.name} won")
                            return resultOfGame
                        }
                        DRAW -> {
                            activePlayer.score += 1
                            activePlayer.next().score += 1
                            println("It is a draw")
                            return resultOfGame
                        }
                        NO_WINNER -> swapActivePlayer()
                    }
                }
            }
        }
    }

    init {
        println("Connect Four")
        println("First player's name:")
        val player1 = Player(name = readln(), color = Color.RED, score = 0)
        println("Second player's name:")
        val player2 = Player(name = readln(), color = Color.YELLOW, score = 0)
        players = listOf(player1, player2)
        while (true) {
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
            when (val result: ResultOfDimension = readDimensions(readln())) {
                is ErrorOfDimension -> println(result.message)
                is SuccessOfDimension -> {
                    rows = result.rows
                    cols = result.cols
                    board = List(rows) { List(cols) { Disk(Color.EMPTY) } }
                    break
                }
            }
        }
        numberOfGames = choiceNumberOfGames()
        gamesCounter = 1
        println(players.joinToString(separator = " VS ", transform = Player::name))
        println("$rows X $cols board")
        println(if (numberOfGames == 1) "Single game" else "Total $numberOfGames games\nGame #$gamesCounter")
        do {
            board.clear()
            if (gamesCounter > 1) println("Game #$gamesCounter")
            println(board.asString())
            activePlayer = players[(gamesCounter - 1) % players.size]
            if (play() == NO_WINNER) break
            println("Score")
            println(players.joinToString(" ") { "${it.name}: ${it.score}" })
        } while (++gamesCounter <= numberOfGames)
        println("Game over!")
    }
}

fun main() { ConnectFour() }