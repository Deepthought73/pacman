package model.ghosts

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import model.Direction
import model.Entity
import model.GameBoard
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

abstract class Ghost(animations: Map<Direction, Animation>, game: Stage) : Entity(animations, game) {

    private var decisionCooldown = 0
    protected var isScattering = true
    private var timer = TimeSpan(0.0)
    private val scatterChanges = mutableListOf(7, 27, 34, 54, 59, 79, 84)

    override fun getSpeed(): Double {
        return 0.75
    }

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addUpdater(fun Stage.(dt: TimeSpan) {
            nextDirection = calculateNextDirection(gameBoard)

            scatterCount(dt)
        })
    }

    abstract fun getTarget(gameBoard: GameBoard): Pair<Int, Int>

    private fun scatterCount(dt: TimeSpan) {
        timer += dt

        if (timer.seconds.toInt() in scatterChanges) {
            scatterChanges.remove(timer.seconds.toInt())
            isScattering = !isScattering
        }
    }

    private fun calculateNextDirection(gameBoard: GameBoard): Direction {
        if (decisionCooldown-- <= 0) {
            val target = getTarget(gameBoard)
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

            decisionCooldown = 3
            return minDirection
        } else return nextDirection
    }

}