package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction
import model.GameBoard
import model.offset
import kotlin.math.roundToInt

class Pinky private constructor(animations: Map<Direction, Animation>, game: Stage) : Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage, offset: Int): Pinky {
            return Pinky(Animation.createDirectoryAnimationMap("ghosts/pinky"), game)
        }
    }

    init {
        image.xy(26 * 4, 21 * 4 + offset)
    }

    override fun getTarget(gameBoard: GameBoard): Pair<Int, Int> {
        return if (isScattering)
            Pair(0, offset)
        else {
            var x = gameBoard.pacman.getX().roundToInt()
            var y = gameBoard.pacman.getY().roundToInt()
            when (gameBoard.pacman.direction) {
                Direction.UP -> y -= 8 * 4
                Direction.DOWN -> y += 8 * 4
                Direction.RIGHT -> x += 8 * 4
                Direction.LEFT -> x -= 8 * 4
            }
            Pair(x, y)
        }
    }

}