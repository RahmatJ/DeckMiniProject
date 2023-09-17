package state

import Game.BlackJackGame
import board.Board
import board.BoardBuilder
import configuration.Configuration
import player.Player

open class BlackJackState : State() {
    override val configuration: Configuration = Configuration.getInstance()
    override val name: String
        get() = "Black Jack State"

    override fun execute(): State {
        return BlackJackState()
    }
}

class InitBlackJackState() : BlackJackState() {
    override val name: String
        get() = "Initial State"

    override fun execute(): State {
        println("=======INITIATING GAME=======")
        print("Insert Board name: ")
        val boardName: String = readln()
        val builder = BoardBuilder().setName(boardName).setGame(BlackJackGame())
        return AddDealerState(builder)
    }
}

class AddDealerState(private var builder: BoardBuilder) : BlackJackState() {
    override val name: String
        get() = "Add Dealer State"

    override fun execute(): State {
        builder.addDealer()
        return AddPlayerState(builder)
    }
}

class AddPlayerState(private var builder: BoardBuilder) : BlackJackState() {
    override val name: String
        get() = "Add Player State"

    override fun execute(): State {
        while (true) {
            print("Player ${builder.getPlayerCount() + 1}: \n")
            print("Insert Player Name: ")
            val playerName = readln()
            val initialBalance = 1000
            println("Player Initial balance: $initialBalance")

            builder.addPlayer(playerName, initialBalance)
            if (builder.getPlayerCount() >= 4) {
                builder.addPlayer(builder.getDealer())
                return SetupBoardState(builder)
            }
            continue
        }
    }
}

class SetupBoardState(private var builder: BoardBuilder) : BlackJackState() {
    override val name: String
        get() = "Setup Board State"

    override fun execute(): State {
        builder.setup()

        val board: Board = builder.build()
        configuration.setBoard(board)
        return ResetConditionState()
    }
}

class ResetConditionState() : BlackJackState() {
    private val board: Board = configuration.getBoard()
    private val turnCounter: Int = configuration.getTurnCount()
    private val baseAmount: Int = configuration.getBetAmount()

    override val name: String
        get() = "Reset Condition"

    override fun execute(): State {
        println("===============")
        println("Current Turn: $turnCounter")
        println("Base Bet amount: $baseAmount")
        println("===============")
        println("Player State:")
        board.getPlayer().forEach {
            it.printBalance()
        }
        println("===============")

        if (turnCounter != 0) {
            board.resetDeck()
            board.resetPlayersHand()
        }

        return AddBetState()
    }

}

class AddBetState() : BlackJackState() {
    private val board: Board = configuration.getBoard()
    private val baseAmount: Int = configuration.getBetAmount()

    override val name: String
        get() = "Add Bet State"

    override fun execute(): State {
        val deletedPlayer = mutableListOf<Player>()
        board.getPlayer().forEach loop@{
            if (!it.isPlayer()) {
                return@loop
            }
            val balance = it.getBalance()
            if (balance <= 0) {
                deletedPlayer.add(it)
                return@loop
            }
            val betAmount = if (balance > baseAmount) baseAmount else balance

            println("Add to current pool: ${it.getName()}")
            Board.calculateBet(board, it, betAmount)
        }

        if (deletedPlayer.isNotEmpty()) {
            //        remove player
            deletedPlayer.forEach {
                println("${it.getName()} Leave The Game !!!")
                board.getPlayer().remove(it)
            }
            val newPlayerCount = Board.getPlayerCount() - deletedPlayer.size
            Board.updatePlayerCount(newPlayerCount)
            println("Updating Player Count to: $newPlayerCount")
        }

        println("Current Pool: ${board.getCurrentPool()}")

        return DistributeCardState()
    }
}

class DistributeCardState() : BlackJackState() {
    private val board: Board = configuration.getBoard()

    override val name: String
        get() = "Distribute Card State"

    override fun execute(): State {
        if (board.getPlayer().size <= 2) {
            return ChooseWinnerState()
        }
        board.distributeCard()

        configuration.setCurrentPlayerIndex(0)

        return PlayerTurnState()
    }
}

class PlayerTurnState() : BlackJackState() {
    private val board: Board = configuration.getBoard()
    private val baseAmount: Int = configuration.getBetAmount()
    private var indexPlayer: Int = configuration.getCurrentPlayerIndex()

    override val name: String
        get() = "Player Turn State"

    override fun execute(): State {
        if (board.getPlayer().size <= 2) {
            return ChooseWinnerState()
        }

        val player = board.getPlayerByIndex(indexPlayer)
        println("Current Player: ${player.getName()}")
        if (player.requestCard()) {
            println("Requesting Card... ")
            val balance = player.getBalance()
            val betAmount = if (balance > baseAmount) baseAmount else balance

            Board.calculateBet(board, player, amount = betAmount)
            val card = board.getDeck().distribute()
            board.addPlayerHand(indexPlayer, card, isRequest = true)
        }

        indexPlayer += 1
        configuration.setCurrentPlayerIndex(indexPlayer)

        if (indexPlayer == board.getPlayer().size) {
            return ChooseWinnerState()
        }

        return PlayerTurnState()
    }
}

class ChooseWinnerState() : BlackJackState() {
    private val board: Board = configuration.getBoard()
    private val turnCounter: Int = configuration.getTurnCount()

    override val name: String
        get() = "Choose Winner State"

    private fun getTurnWinner(): List<Player> {
        var max = 0
        var winners = mutableListOf<Player>()
        board.getPlayer().forEach loop@{
            val playerValue = it.getValue()

            if (playerValue < max) {
                return@loop
            }

            if (playerValue == max) {
                winners.add(it)
                return@loop
            }

            max = playerValue
            winners = mutableListOf(it)
        }

        return winners
    }

    private fun getWinner(): Player {
        val players: List<Player> = board.rankPlayer()
        val reversed = players.reversed()

        println("Ranking Player: ")
        reversed.forEach {
            println("Name: ${it.getName()}| Balance: ${it.getBalance()}")
        }

        return reversed[0]
    }

    override fun execute(): State {
        board.revealHand()
        val winners = getTurnWinner()

        //        Add Current Pool
        board.giveCurrentPool(winners)

        if (turnCounter == 10 || board.getPlayer().size <= 2) {
            val winnerPlayer = getWinner()
            configuration.setWinner(winnerPlayer)

            return DisplayWinnerState()
        }

        configuration.addCounter()
        configuration.addBetAmount(50)

        return ResetConditionState()
    }
}

class DisplayWinnerState() : BlackJackState() {
    private val player: Player = configuration.getWinner()

    override val name: String = "Display Winner State"

    override fun execute(): State {
        val winnerString = if (player.isPlayer()) "PLAYER" else "DEALER"

        println("THE WINNER IS THE $winnerString WITH THIS DATA")
        println(player)
        return Done()
    }

}

class Done() : BlackJackState() {
    override val name: String
        get() = "Done State"

    override var isDone: Boolean = true

    override fun execute(): State {
        println("Thanks for playing")
        return Done()
    }
}