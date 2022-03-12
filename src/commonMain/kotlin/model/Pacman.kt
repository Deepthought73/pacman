package model

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import model.Direction.*
import model.ghosts.Ghost

class Pacman private constructor(
    animations: Map<Direction, Animation>,
    game: Stage,
    offset: Int
) :
    Entity(animations, game) {

    companion object {
        suspend fun create(game: Stage): Pacman {
            return Pacman(
                Animation.createDirectoryAnimationMap("pacman"),
                game,
                offset
            )
        }
    }

    init {
        image.xy(26 * 4, 45 * 4 + offset)
    }

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addUpdater(fun Stage.(_: TimeSpan) {
            if (input.keys.pressing(Key.LEFT)) nextDirection = LEFT
            else if (input.keys.pressing(Key.RIGHT)) nextDirection = RIGHT
            else if (input.keys.pressing(Key.UP)) nextDirection = UP
            else if (input.keys.pressing(Key.DOWN)) nextDirection = DOWN
            gameBoard.checkDotCollision()
            gameBoard.checkPowerPalletCollision(gameBoard)
        })
    }

    override fun getSpeed(): Double {
        return if (Ghost.isOneFrightened) 0.9
        else 0.8
    }

}