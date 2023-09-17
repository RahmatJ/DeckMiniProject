package deck

import Game.CardGame
import card.ICard

class Deck(private val game: CardGame) {
    private var cardArray = mutableListOf<ICard>()

    init {
        initDeck()
    }

    private fun initDeck() {
        cardArray = game.initDeck()
    }

    fun shuffleDeck() {
        this.cardArray.shuffle()
    }

    fun distribute(): ICard {
        return this.cardArray.removeLast()
    }

    fun resetDeck() {
        initDeck()
        shuffleDeck()
    }
}