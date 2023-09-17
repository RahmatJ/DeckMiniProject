package card


class BlackJackCard() : ICard {
    private var suit: String = "SPADE"
    private var rank: String = "A"

    constructor(suit: String, rank: String) : this() {
        this.suit = suit
        this.rank = rank
    }

    //    Should be defined by game, might need use strategy pattern
    override fun getValue(): Int {
        val cardValue = when (rank.uppercase()) {
            "A" -> 11
            "J" -> 10
            "Q" -> 10
            "K" -> 10
            else -> rank.toInt()
        }
        return cardValue
    }

    private fun suitToUnicode(suit: String): String {
        return when (suit.lowercase()) {
            "spade" -> "\u2660"
            "club" -> "\u2663"
            "diamond" -> "\u2666"
            "heart" -> "\u2665"
            else -> throw Exception("SUIT UNDEFINED")
        }
    }

    override fun toString(): String {
        return "[${rank}${suitToUnicode(suit)}]"
    }
}

