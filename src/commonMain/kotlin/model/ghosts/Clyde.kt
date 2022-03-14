package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction
import model.GameBoard
import model.offset
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Clyde private constructor(animations: Map<Direction, Animation>, game: Stage) : Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage, offset: Int): Clyde {
            return Clyde(Animation.createDirectoryAnimationMap("ghosts/clyde"), game)
        }
    }

    init {
        initialPos()
    }

    override fun initialPos() {
        image.xy(30 * 4, 28 * 4 + offset)
    }

    override fun isInBox(gameBoard: GameBoard): Boolean {
        return gameBoard.dotObjects.size / 240.0 >= 0.33
    }

    override fun getTarget(gameBoard: GameBoard): Pair<Int, Int> {
        val pacmanX = gameBoard.pacman.getX()
        val pacmanY = gameBoard.pacman.getY()

        val distanceToPacman = sqrt((pacmanX - getX()).pow(2) + (pacmanY - getY()).pow(2))
        return if (isDead)
            return super.getTarget(gameBoard)
        else if (isScattering || distanceToPacman < 8 * 8)
            Pair(0, 248 + offset)
        else
            Pair(gameBoard.pacman.getX().roundToInt(), gameBoard.pacman.getY().roundToInt())
    }

}