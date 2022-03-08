package model

import Animation
import com.soywiz.kmem.toIntCeil
import com.soywiz.kmem.toIntFloor
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image

abstract class Entity(
    protected val animations: Map<Directory, Animation>,
    protected val game: Stage
) {

    protected var direction = Directory.DOWN
    protected val image = game.image(animations[direction]!!.next())

    open fun addListener(gameBoard: GameBoard) {
        game.addUpdater {
            shift(getSpeed())
            if (gameBoard.hasCollision((image.x / 4).toIntCeil(), (image.y / 4).toIntCeil(), 4, 4)
                || gameBoard.hasCollision((image.x / 4).toIntFloor(), (image.y / 4).toIntFloor(), 4, 4)
            ) {
                reShift(getSpeed())
            }
        }
    }

    protected abstract fun getSpeed(): Double

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