import Game.Game
import state.InitBlackJackState
import state.State

fun main(args: Array<String>) {
    val state: State = InitBlackJackState()

    Game.setGameState(state)
    Game.run()
}