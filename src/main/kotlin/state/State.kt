package state

import configuration.Configuration

abstract class State {
    abstract val configuration: Configuration
    abstract val name: String
    open var isDone: Boolean = false
    abstract fun execute(): State
}

