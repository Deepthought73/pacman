package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction
import model.GameBoard
import model.offset
import kotlin.math.roundToInt

class Blinky private constructor(animations: Map<Direction, Animation>, game: Stage) : Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage, offset: Int): Blinky {
            return Blinky(Animation.createDirectoryAnimationMap("ghosts/blinky"), game)
        }
    }

    init {
        initialPos()
    }

    override fun initialPos() {
        image.xy(26 * 4, 21 * 4 + offset)
    }

    override fun isInBox(gameBoard: GameBoard): Boolean {
        return false
    }

    override fun getTarget(gameBoard: GameBoard): Pair<Int, Int> {
        return if (isDead)
            return super.getTarget(gameBoard)
        else if (isScattering)
            Pair(224, offset)
        else
            Pair(gameBoard.pacman.getX().roundToInt(), gameBoard.pacman.getY().roundToInt())
    }

}