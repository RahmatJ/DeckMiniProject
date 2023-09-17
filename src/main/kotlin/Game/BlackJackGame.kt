package Game

import card.BlackJackCard
import card.BlackJackCardFactory
import card.ICard

class BlackJackGame() : CardGame() {
    override val gameName: String = "Black Jack"

    override fun initDeck(): MutableList<ICard> {
        val suitList = listOf<String>("SPADE", "HEART", "CLUB", "DIAMOND")
        val cardArray = mutableListOf<ICard>()
        suitList.forEach {
            for (i in 1..13) {
                val rank = when (i) {
                    1 -> "A"
                    11 -> "J"
                    12 -> "Q"
                    13 -> "K"
                    else -> i.toString()
                }

                val blackJackCard: BlackJackCard = BlackJackCardFactory.createCard(it, rank)
                cardArray.add(blackJackCard)
            }
        }
        return cardArray
    }

    override fun getValue(cards: MutableList<ICard>): Int {
        var value = 0
        cards.forEach {
            var currentValue = it.getValue()
            if (currentValue == 11 && value > 10) {
                currentValue = 1
            }
            value += currentValue
        }
        if (value > 21) value = 0
        return value
    }

    override fun isHandReady(hand: MutableList<ICard>): Boolean {
        return hand.size == 2
    }
}