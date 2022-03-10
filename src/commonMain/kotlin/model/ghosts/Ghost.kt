package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import model.Direction
import model.Entity
import model.GameBoard
import kotlin.math.pow
import kotlin.math.sqrt

abstract class Ghost(animations: Map<Direction, Animation>, game: Stage) : Entity(animations, game) {

    override fun getSpeed(): Double {
        return 0.75
    }

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addUpdater {
            nextDirection = calculateNextDirection(gameBoard)
        }
    }

    abstract fun getTarget(): Pair<Int, Int>

    private fun calculateNextDirection(gameBoard: GameBoard): Direction {
        val target = getTarget()
        var minDirection = direction
        var minDistance = Double.POSITIVE_INFINITY

        for (dir in Direction.values()
            .filter { it != direction.invert() }
            .filter {
                var shiftCount = 0
                var hadCollision = false
                while (shiftCount < 10 && !hadCollision) {
                    shift(it)
                    shiftCount++
                    hadCollision = hasCollision(gameBoard)
                }
                for (i in 0 until shiftCount)
                    reShift(it)
                !hadCollision
            }
        ) {
            shift(dir)
            val distance = sqrt((target.first - image.x).pow(2) + (target.second - image.y).pow(2))
            reShift(dir)

            if (distance < minDistance) {
                minDistance = distance
                minDirection = dir
            }
        }

        return minDirection
    }

}