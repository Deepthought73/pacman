package model

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.slice
import model.Directory.*

class Pacman private constructor(
    animations: Map<Directory, Animation>,
    game: Stage
) :
    Entity(animations, game) {

    companion object {
        private const val SPEED = 0.75

        suspend fun create(game: Stage): Pacman {
            return Pacman(
                Animation.createDirectoryAnimationMap("pacman"),
                game
            )
        }
    }

    init {
        image.xy(26 * 4, 45 * 4)
    }

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addUpdater {
            image.bitmap = animations[direction]!!.next().slice()
        }

        game.addUpdater(fun Stage.(_: TimeSpan) {
            if (input.keys.pressing(Key.LEFT)) direction = LEFT
            else if (input.keys.pressing(Key.RIGHT)) direction = RIGHT
            else if (input.keys.pressing(Key.UP)) direction = UP
            else if (input.keys.pressing(Key.DOWN)) direction = DOWN
        })
    }

    override fun getSpeed(): Double {
        return 0.75
    }

}