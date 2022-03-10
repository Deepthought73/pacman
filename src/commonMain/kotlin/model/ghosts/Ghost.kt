package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import model.Direction
import model.Entity

abstract class Ghost(animations: Map<Direction, Animation>, game: Stage) : Entity(animations, game) {

    override fun getSpeed(): Double {
        return 0.75
    }

    init {
        game.addUpdater {
            nextDirection = calculateNextDirection()
        }
    }

    abstract fun getAim(): Pair<Int, Int>

    fun calculateNextDirection(): Direction {
        return Direction.DOWN
    }

}