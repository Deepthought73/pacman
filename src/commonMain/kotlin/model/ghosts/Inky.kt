package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction
import model.GameBoard
import model.offset
import kotlin.math.roundToInt

class Inky private constructor(animations: Map<Direction, Animation>, game: Stage) : Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage, offset: Int): Inky {
            return Inky(Animation.createDirectoryAnimationMap("ghosts/inky"), game)
        }
    }

    init {
        initialPos()
    }

    override fun initialPos() {
        image.xy(26 * 4, 21 * 4+ offset)
    }

    override fun getTarget(gameBoard: GameBoard): Pair<Int, Int> {
        return if (isScattering)
            Pair(224, 248 + offset)
        else if (isDead)
            return super.getTarget(gameBoard)
        else {
            var pacmanX = gameBoard.pacman.getX().roundToInt()
            var pacmanY = gameBoard.pacman.getY().roundToInt()
            val blinkyX = gameBoard.ghosts[0].getX().roundToInt()
            val blinkyY = gameBoard.ghosts[0].getY().roundToInt()

            when (gameBoard.pacman.direction) {
                Direction.UP -> pacmanY -= 8 * 2
                Direction.DOWN -> pacmanY += 8 * 2
                Direction.RIGHT -> pacmanX += 8 * 2
                Direction.LEFT -> pacmanX -= 8 * 2
            }

            val vecX = 2 * (pacmanX - blinkyX)
            val vecY = 2 * (pacmanY - blinkyY)

            Pair(blinkyX + vecX, blinkyY + vecY)
        }
    }

}