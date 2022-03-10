package model

import Animation
import com.soywiz.kmem.toIntCeil
import com.soywiz.kmem.toIntFloor
import com.soywiz.kmem.toIntRound
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image

abstract class Entity(
    protected val animations: Map<Directory, Animation>,
    protected val game: Stage
) {

    protected var direction = Directory.DOWN
    protected var nextDirection = Directory.DOWN

    protected val image = game.image(animations[direction]!!.next())


    open fun addListener(gameBoard: GameBoard) {
        game.addUpdater {
            move(gameBoard)
        }
    }

    protected abstract fun getSpeed(): Double

    private fun move(gameBoard: GameBoard) {
        val oldDirection = direction
        direction = nextDirection
        if (oldDirection != direction) {
            var collision = false
            var shiftCounter = 0
            for (i in 1..10) {
                shift(getSpeed())
                collision = collision || gameBoard.hasCollision((image.x / 4).toIntRound(), (image.y / 4).toIntRound(), 4, 4)
                shiftCounter ++
                if (collision) {
                    break
                }
            }
            for (i in 1..shiftCounter) {
                reShift(getSpeed())
            }
            if (collision) {
                direction = oldDirection
            }
            shift(getSpeed())
            if (gameBoard.hasCollision((image.x / 4).toIntRound(), (image.y / 4).toIntRound(), 4, 4)) {
                reShift(getSpeed())
            }
        } else {
            shift(getSpeed())
            if (gameBoard.hasCollision((image.x / 4).toIntRound(), (image.y / 4).toIntRound(), 4, 4)) {
                reShift(getSpeed())
            }
        }
    }

    private fun shift(distance: Double) {
        when (direction) {
            Directory.UP -> image.y -= distance
            Directory.DOWN -> image.y += distance
            Directory.RIGHT -> image.x += distance
            Directory.LEFT -> image.x -= distance
        }
    }

    private fun reShift(distance: Double) {
        when (direction) {
            Directory.UP -> image.y += distance
            Directory.DOWN -> image.y -= distance
            Directory.RIGHT -> image.x -= distance
            Directory.LEFT -> image.x += distance
        }
    }

}