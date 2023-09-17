package card

class BlackJackCardFactory() {
    companion object {
        fun createCard(suit: String, rank: String): BlackJackCard {
            return BlackJackCard(suit, rank)
        }
    }
}