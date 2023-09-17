package board

import Game.CardGame
import card.ICard
import deck.Deck
import player.Player

class Board() {
    private lateinit var game: CardGame
    private lateinit var deck: Deck
    private var name: String = ""
    private var players = mutableListOf<Player>()
    private var currentPool = 0
    private var dealer: Player? = null

    fun setup() {
        println("Welcome to Board $name\n")
        print("Shuffling deck ...")
        deck.shuffleDeck()
        println("Done Shuffling")
    }

    fun setDealer(player: Player) {
        if (dealer == null) {
            player.setAsDealer()
            this.dealer = player
            return
        }
    }

    fun setGame(game: CardGame) {
        this.game = game
        deck = Deck(game)
    }

    fun getGame(): CardGame {
        return this.game
    }

    fun rankPlayer(): List<Player> {
        return players.sortedWith(compareBy { it.getBalance() })
    }

    fun getDealer(): Player {
        if (this.dealer == null) {
            throw Exception("No Dealer")
        }
        return this.dealer!!
    }

    fun addPlayer(player: Player) {
        this.players.add(player)
        playerCount += 1
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getCurrentPool(): Int {
        return this.currentPool
    }

    fun addCurrentPool(amount: Int) {
        this.currentPool += amount
    }

    fun giveCurrentPool(winners: List<Player>) {
        val winnerPrize = this.currentPool / winners.size
        val winnersId = getListOfId(winners)
        println(
            "We have ${winners.size} winner${if (winners.size == 1) "" else "s"}, " +
                    "Each Winner will get $winnerPrize"
        )
        players.filter { winnersId.contains(it.getId()) }.run {
            forEach {
                if (it.isPlayer()) {
                    it.addBalance(winnerPrize)
                }
            }
        }
        this.currentPool = 0
    }

    fun getPlayer(): MutableList<Player> {
        return this.players
    }

    fun getPlayerByIndex(index: Int): Player {
        return this.players.get(index)
    }

    fun getDeck(): Deck {
        return this.deck
    }

    fun resetPlayersHand() {
        players.forEach {
            it.resetHand()
        }
    }

    fun addPlayerHand(index: Int, card: ICard, isRequest: Boolean = false) {
        players[index].addHand(card, isRequest)
        println(players[index].showHand())
    }

    //    might update this use strategy pattern
    fun distributeCard() {
        var readyCount = 0

        while (readyCount != getPlayerCount()) {
            players.forEach {
                if (!it.isHandReady()) {
                    val card: ICard = deck.distribute()
                    it.addHand(card)

                    if (it.isHandReady()) {
                        println("Player ${it.getName()} is Ready ...")
                        it.printNameAndHand()
                        readyCount += 1
                    }
                }
            }
        }

        println("Done Distribute")

    }

    fun resetDeck() {
        deck.resetDeck()
    }

    fun revealHand() {
        println("Revealing Hand ...")
        players.forEach {
            it.printNameAndHand(withValue = true)
        }
    }

    override fun toString(): String {
        var data = "Dealer: ${dealer!!.getName()}\n"
        for (i in 1..playerCount) {
            data += "$i. ${players[i - 1].getName()}\n"
        }
        return data
    }

    companion object {
        @JvmStatic
        private var playerCount: Int = 0

        fun getPlayerCount(): Int {
            return playerCount
        }

        fun updatePlayerCount(count: Int) {
            playerCount = count
        }

        fun calculateBet(board: Board, player: Player, amount: Int = 20) {
            player.addBalance(amount * -1)
            if (player.getBalance() == 0) {
                println("${player.getName()} ALL IN !!!")
            }
            board.addCurrentPool(amount)
        }

        private fun getListOfId(players: List<Player>): List<Int> {
            return players.map { it.getId() }
        }
    }
}