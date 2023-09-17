package board

import Game.CardGame
import player.Player
import player.PlayerFactory

class BoardBuilder() {
    private var currentBoard: Board = Board()

    fun setName(name: String): BoardBuilder {
        this.currentBoard.setName(name)
        return this
    }

    fun setGame(game: CardGame): BoardBuilder {
        println("Set Game : ${game.gameName} ... ")
        this.currentBoard.setGame(game)
        println("Done")
        return this
    }

    fun addDealer(): BoardBuilder {
        print("Adding dealer ... ")
        val dealer = PlayerFactory.createDealer(this.currentBoard.getGame())
        this.currentBoard.setDealer(dealer)
        println("Done")
        return this
    }

    fun getDealer(): Player {
        return this.currentBoard.getDealer()
    }

    fun addPlayer(name: String, balance: Int): BoardBuilder {
        print("Adding player with name $name and Balance $balance ... ")
        val player = PlayerFactory.createPlayer(name, balance = balance, game = this.currentBoard.getGame())
        this.currentBoard.addPlayer(player)
        println("Done")
        return this
    }

    fun addPlayer(player: Player): BoardBuilder {
        print("Adding player with name ${player.getName()} and Balance ${player.getBalance()} ... ")
        this.currentBoard.addPlayer(player)
        println("Done")
        return this
    }

    fun getPlayerCount(): Int {
        return Board.getPlayerCount()
    }

    fun setup(): BoardBuilder {
        this.currentBoard.setup()
        return this
    }

    fun build(): Board {
        return this.currentBoard
    }
}