package model

import Animation
import com.soywiz.kmem.toIntRound
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image
import com.soywiz.korim.bitmap.slice

abstract class Entity(
    private val animations: Map<Direction, Animation>,
    protected val game: Stage
) {

    protected var direction = Direction.DOWN
    protected var nextDirection = Direction.DOWN

    protected val image = game.image(animations[direction]!!.next())

    open fun addListener(gameBoard: GameBoard) {
        game.addUpdater {
            move(gameBoard)
            image.bitmap = animations[direction]!!.next().slice()
        }
    }

    protected abstract fun getSpeed(): Double

    private fun move(gameBoard: GameBoard) {
        val oldDirection = direction
        direction = nextDirection
        if (oldDirection != direction) {
            var shiftCounter = 0
            for (i in 1..10) {
                shift(nextDirection)
                shiftCounter++
                if (hasCollision(gameBoard)) {
                    direction = oldDirection
                    break
                }
            }
            for (i in 1..shiftCounter) {
                reShift(nextDirection)
            }
        }
        shift()
        if (hasCollision(gameBoard)) {
            reShift()
        } else {
            if (direction == Direction.DOWN || direction == Direction.UP) {
                gridVertical()
            } else {
                gridHorizontal()
            }
        }
    }

    private fun gridHorizontal() {
        println("grid: "+image.y+" "+(image.y/4).toIntRound()*4)
        image.y = ((image.y/4).toIntRound()*4).toDouble()
    }

    private fun gridVertical() {
        println("grid: "+image.y+" "+(image.y/4).toIntRound()*4)
        image.x = ((image.x/4).toIntRound()*4).toDouble()
    }

    private fun hasCollision(gameBoard: GameBoard): Boolean {
        return gameBoard.hasCollision((image.x / 4).toIntRound(), (image.y / 4).toIntRound(), 4, 4)
    }

    private fun shift(direction_: Direction = direction) {
        val distance: Double = getSpeed()
        when (direction_) {
            Direction.UP -> image.y -= distance
            Direction.DOWN -> image.y += distance
            Direction.RIGHT -> {
                image.x = image.x + distance
                image.x %= 56 * 4
            }
            Direction.LEFT -> {
                image.x = image.x - distance
                if (image.x < 0) {
                    image.x += 56 * 4
                }
            }
        }
    }

    private fun reShift(direction_: Direction = direction) {
        val distance = getSpeed()
        when (direction_) {
            Direction.UP -> image.y += distance
            Direction.DOWN -> image.y -= distance
            Direction.RIGHT -> image.x -= distance
            Direction.LEFT -> image.x += distance
        }
    }

    private fun shift(distance: Double) {
        when (direction) {
            Direction.UP -> image.y -= distance
            Direction.DOWN -> image.y += distance
            Direction.RIGHT -> image.x += distance
            Direction.LEFT -> image.x -= distance
        }
    }

}