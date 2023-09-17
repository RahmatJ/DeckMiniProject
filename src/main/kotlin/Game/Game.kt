package Game

import state.State

abstract class Game {
    companion object {
        private lateinit var gameState: State

        fun setGameState(currentState: State) {
            gameState = currentState
        }

        fun run() {
            while (true) {
                println("============================")
                println("Current State: ${gameState.name}")
                println("============================")
                gameState = gameState.execute()
                if (gameState.isDone) {
                    gameState.execute()
                    break
                }

                println("Wait ...")
                println("============================")
            }
        }
    }
}