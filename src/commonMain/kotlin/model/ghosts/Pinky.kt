package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction
import model.GameBoard
import model.offset

class Pinky private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage, offset: Int): Pinky {
            return Pinky(Animation.createDirectoryAnimationMap("ghosts/pinky"), game)
        }
    }

    init {
        image.xy(26 * 4, 21 * 4+ offset)
    }

    override fun getTarget(gameBoard: GameBoard): Pair<Int, Int> {
        return Pair(0, 0)
    }

}