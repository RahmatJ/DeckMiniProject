package Game

import card.ICard

abstract class CardGame : Game() {
    abstract val gameName: String
    abstract fun initDeck(): MutableList<ICard>
    abstract fun getValue(cards: MutableList<ICard>): Int
    abstract fun isHandReady(hand: MutableList<ICard>): Boolean
}