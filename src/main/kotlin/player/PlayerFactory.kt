package player

import Game.CardGame

class PlayerFactory {
    companion object {
        fun createPlayer(name: String, balance: Int, game: CardGame): Player {
            return Player(name, balance = balance, game = game)
        }

        fun createDealer(game: CardGame): Player {
            return Player("DEALER", game = game)
        }
    }
}