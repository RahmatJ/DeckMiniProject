package player

import Game.CardGame
import card.ICard

class Player() {
    private lateinit var name: String
    private var balance: Int = 0
    private var hand: MutableList<ICard> = mutableListOf<ICard>()
    private var isDealer: Boolean = false
    private var id = 0
    private lateinit var game: CardGame

    init {
        setId(currentIndex)
        currentIndex += 1
    }

    constructor(name: String, game: CardGame, balance: Int = 0) : this() {
        this.name = name
        this.balance = balance
        this.game = game
    }

    private fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return this.id
    }

    fun addHand(card: ICard, isRequest: Boolean = false) {
        if (!isRequest && isHandReady()) {
            return
        }

        this.hand.add(card)
    }

    fun addBalance(amount: Int) {
        println("Adding Balance for $name:")
        this.balance += amount
        println("Current Balance: ${this.balance}")
    }

    //    might use strategy pattern to calculate value based on game
    //    for now just use basic
    fun getValue(): Int {
        return game.getValue(hand)
    }

    //    might use strategy pattern to calculate value based on game
    //    for now just use basic
    fun isHandReady(): Boolean {
        return game.isHandReady(hand)
    }

    fun resetHand() {
        hand = mutableListOf<ICard>()
    }

    //    Only for BlackJack
    fun requestCard(): Boolean {
        if (isDealer) return false
        if (balance <= 0) return false
        if (getValue() >= 21) return false
//        try random chance
        val randomInt = (0..10000).random()
        if (randomInt % 2 == 0) return false
        return true
    }

    fun setAsDealer() {
        this.isDealer = true
    }

    fun isPlayer(): Boolean {
        return !isDealer
    }

    fun getName(): String {
        return this.name
    }

    fun getBalance(): Int {
        return this.balance
    }

    fun showHand(): String {
        var data = ""
        hand.forEach { data += "${it} " }
        return data
    }

    fun printBalance() {
        println("$id - $name : ${balance}")
    }

    fun printNameAndHand(withValue: Boolean = false) {
        println("$id - $name : ${showHand()} ${if (withValue) getValue() else ""}")
    }

    override fun toString(): String {
        var data = "ID: $id\n" +
                "Name: $name\n" +
                "Balance: $balance\n" +
                "Hand: \n"
        data += showHand()
        if (getValue() == 21) {
            data += "\n** BLACK JACK **\n"
        }
        return data
    }

    companion object {
        @JvmStatic
        private var currentIndex: Int = 1

        fun getCurrentIndex(): Int {
            return currentIndex
        }
    }
}