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
            if (direction == Directory.DOWN || direction == Directory.UP) {
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

    private fun shift(direction_: Directory = direction) {
        val distance: Double = getSpeed()
        when (direction_) {
            Directory.UP -> image.y -= distance
            Directory.DOWN -> image.y += distance
            Directory.RIGHT -> {
                image.x = image.x + distance
                image.x %= 56*4
            }
            Directory.LEFT -> {
                image.x = image.x - distance
                if (image.x < 0) {
                    image.x += 56*4
                }
            }
        }
    }

    private fun reShift(direction_: Directory = direction) {
        val distance: Double = getSpeed()
        when (direction_) {
            Directory.UP -> image.y += distance
            Directory.DOWN -> image.y -= distance
            Directory.RIGHT -> image.x -= distance
            Directory.LEFT -> image.x += distance
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