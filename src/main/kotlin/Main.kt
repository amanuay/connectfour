package connectfour

fun main() {
    var boardRow = 6      //default values of rows and columns
    var boardColumn = 7
    val inputRegex = Regex("\\d+X\\d+")  //number x number
    var input: String

    println("Connect Four")

    println("First player's name:")
    val firstPlayerName = readln()

    println("Second player's name:")
    val secondPlayerName = readln()

    input = getInput()

    //verify the input
    while (input != "") {
        if (input.matches(inputRegex)) {
            val splitInput = input.split('X').toList()
            if (splitInput[0].toInt() in 5..9 && splitInput[1].toInt() in 5..9) {
                boardRow = splitInput[0].toInt()
                boardColumn = splitInput[1].toInt()
                break
            } else {
                if (splitInput[0].toInt() < 5 || splitInput[0].toInt() > 9) { rowMessage() }
                if (splitInput[1].toInt() < 5 || splitInput[1].toInt() > 9) { columnMessage() }
                input = getInput()
            }
        }
        if (!input.matches(inputRegex)) {
            invalidInputMessage()
            input = getInput()
        }
    }

    val gameNumberTotal = gameNumberInput()

    println("$firstPlayerName VS $secondPlayerName")
    println("$boardRow X $boardColumn board")

    if (gameNumberTotal == 1) {
        println("Single game")
    } else {
        println("Total $gameNumberTotal games")
    }

    var firstPlayerScore = 0
    var secondPlayerScore = 0

    for (i in 1..gameNumberTotal) {
        if (gameNumberTotal > 1) {
            println("Game #$i")
        }

        var playerName: String
        if (i % 2 == 0) {
            playerName = secondPlayerName
            Token.token = Token.star
        } else {
            playerName = firstPlayerName
            Token.token = Token.circle
        }

        val newDrawBoard = newDrawBoard(boardRow,boardColumn)
        printMegaList(newDrawBoard)

        println("$playerName's turn:")
        var playerInput = readln()


        while (playerInput != "end") {
            if (!isInt(playerInput)) {
                println("Incorrect column number")
                playerInput = getGameInput(playerName)
            }
            if (isInt(playerInput)) {
                if(playerInput.toInt() in 1..boardColumn) {
                    if (newDrawBoard[0][playerInput.toInt() * 2 - 1] == ' '){
                        placeToken(newDrawBoard, playerInput.toInt())
                        printMegaList(newDrawBoard)
                        //check the winning condition, if not get playerInput
                        if (hasWon(newDrawBoard)) {
                            if (playerName == firstPlayerName) {
                                firstPlayerScore += 2
                            }
                            if (playerName == secondPlayerName) {
                                secondPlayerScore += 2
                            }
                            println("Player $playerName won")

                            println("Score")
                            println("$firstPlayerName: $firstPlayerScore $secondPlayerName: $secondPlayerScore")

                            break
                        }
                        if (!newDrawBoard[0].contains(' ')) {
                            firstPlayerScore += 1
                            secondPlayerScore += 1
                            println("It is a draw")

                            println("Score")
                            println("$firstPlayerName: $firstPlayerScore $secondPlayerName: $secondPlayerScore")

                            break
                        }
                        //alternating between player turns
                        if (playerName == secondPlayerName) {
                            playerName = firstPlayerName
                            Token.token = Token.circle
                        } else {
                            playerName = secondPlayerName
                            Token.token = Token.star
                        }
                        playerInput = getGameInput(playerName)

                    } else {
                        println("Column $playerInput is full")
                        playerInput = getGameInput(playerName)
                    }
                } else {
                    println("The column number is out of range (1 - $boardColumn)")
                    playerInput = getGameInput(playerName)
                }
            }
        }
        if (playerInput == "end") break
    }

    println("Game over!")
}

fun invalidInputMessage() {
    println("Invalid input")
}

fun rowMessage() {
    println("Board rows should be from 5 to 9")
}

fun columnMessage() {
    println("Board columns should be from 5 to 9")
}

fun getInput(): String {
    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")
    return readln().replace(" ", "").replace("\t", "").uppercase()
}

//1 2 3 4 5  6  7
//1 3 5 7 9 11 13 column number corresponding to the spaces in the list


fun isInt (string: String): Boolean {
    return string.toIntOrNull() != null
}

fun getGameInput(player: String): String {
    println("$player's turn:")
    return readln()
}

fun printMegaList(list: List<List<Char>>) {
    val column = list[0].size/2 - 1
    val row = list.size
    val columnList = List(column){"═╩"}.joinToString(separator = "")

    for (i in 1..column + 1) {
        print(" $i")
    }
    println()
    for (i in 1..row) {
        println(list[i-1].joinToString(separator = ""))
    }
    println("╚$columnList═╝")
}

//initiate the board
fun newDrawBoard(row: Int, column: Int): List<MutableList<Char>> {
    //values are string type
    val rowList = List(column + 1) { "║" }.joinToString(separator = " ")
    return MutableList(row) { rowList.toMutableList() }
}

//edit the board
fun placeToken(rowMegaList: List<MutableList<Char>>,input: Int): List<MutableList<Char>> {
    val row = rowMegaList.size
    val token = Token.token

    for (i in 1..row) {
        if (rowMegaList[row - i][input + input - 1] == ' ') {
            rowMegaList[row - i][input + input - 1] = token
            break
        }
    }
    return rowMegaList
}

//winning condition
fun hasWon(rowMegaList: List<MutableList<Char>>): Boolean {
    //horizontal connect
    for (i in rowMegaList.indices) {
        for (j in 0..rowMegaList[i].size - 8) { //size - 8?
            if (
                rowMegaList[i][j] != ' ' && rowMegaList[i][j] != '║'
                && rowMegaList[i][j] == rowMegaList[i][j + 2]
                && rowMegaList[i][j + 2] == rowMegaList[i][j + 4]
                && rowMegaList[i][j + 4] == rowMegaList[i][j + 6]
            ) {
                return true
            }
        }
    }
    //vertical connect
    for ( i in 0..rowMegaList.size - 4) {
        for (j in rowMegaList[i].indices) {
            if (
                rowMegaList[i][j] != ' ' && rowMegaList[i][j] != '║'
                && rowMegaList[i][j] == rowMegaList[i + 1][j]
                && rowMegaList[i + 1][j] == rowMegaList[i + 2][j]
                && rowMegaList[i + 2][j] == rowMegaList[i + 3][j]
            ) {
                return true
            }
        }
    }
    //diagonal connect left to right(descend)
    for ( i in 0..rowMegaList.size - 4) {
        for (j in 1..rowMegaList[i].size - 5) {
            if (
                rowMegaList[i][j] != ' ' && rowMegaList[i][j] != '║'
                && rowMegaList[i][j] == rowMegaList[i + 1][j + 2]
                && rowMegaList[i + 1][j + 2] == rowMegaList[i + 2][j + 4]
                && rowMegaList[i + 2][j + 4] == rowMegaList[i + 3][j + 6]
            ) {
                return true
            }
        }
    }
    //diagonal connect right to left(ascend)
    for ( i in 0..rowMegaList.size - 4) {
        for (j in 7 until rowMegaList[i].size) {
            if (
                rowMegaList[i][j] != ' ' && rowMegaList[i][j] != '║'
                && rowMegaList[i][j] == rowMegaList[i + 1][j - 2]
                && rowMegaList[i + 1][j - 2] == rowMegaList[i + 2][j - 4]
                && rowMegaList[i + 2][j - 4] == rowMegaList[i + 3][j - 6]
            ) {
                return true
            }
        }
    }
    return false

}

//choosing number of matches
fun gameNumberInput(): Int {
    val numberOfGames: Int
    println("Do you want to play single or multiple games?")
    println("For a single game, input 1 or press Enter")
    println("Input a number of games:")
    val numberOfGamesInput = readln()
    if (numberOfGamesInput == "") {
        numberOfGames = 1
    } else if (isInt(numberOfGamesInput)) {
        if (numberOfGamesInput.toInt() < 1) {
            println("Invalid input")
            numberOfGames = gameNumberInput()
        } else {
            numberOfGames = numberOfGamesInput.toInt()
        }

    } else {
        println("Invalid input")
        numberOfGames = gameNumberInput()
    }
    return numberOfGames
}


class Token {
    companion object {
        const val circle = 'o'
        const val star = '*'
        var token = circle
    }
}


