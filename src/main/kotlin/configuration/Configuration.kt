package configuration

import board.Board
import player.Player

class Configuration() {
    private lateinit var currentBoard: Board
    private var turnCounter: Int = 0
    private var currentBetAmount: Int = 200
    private var currentPlayerIndex: Int = 0
    private lateinit var winner: Player

    fun setBoard(board: Board) {
        this.currentBoard = board
    }

    fun getBoard(): Board {
        return this.currentBoard
    }

    fun addCounter() {
        this.turnCounter += 1
    }

    fun resetCounter() {
        this.turnCounter = 0
    }

    fun getTurnCount(): Int {
        return this.turnCounter
    }

    fun addBetAmount(amount: Int) {
        this.currentBetAmount += amount
    }

    fun setBetAmount(amount: Int) {
        this.currentBetAmount = amount
    }

    fun resetBetAmount() {
        setBetAmount(0)
    }

    fun getBetAmount(): Int {
        return this.currentBetAmount
    }

    fun setCurrentPlayerIndex(index: Int) {
        this.currentPlayerIndex = index
    }

    fun getCurrentPlayerIndex(): Int {
        return this.currentPlayerIndex
    }

    fun setWinner(player: Player) {
        this.winner = player
    }

    fun getWinner(): Player {
        return this.winner
    }

    companion object {
        @JvmStatic
        private var instance: Configuration? = null

        fun getInstance(): Configuration {
            if (instance == null) {
                instance = Configuration()
            }
            return instance as Configuration
        }
    }
}