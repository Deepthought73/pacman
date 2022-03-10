package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction

class Clyde private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Clyde {
            return Clyde(Animation.createDirectoryAnimationMap("ghosts/clyde"), game)
        }
    }

    init {
        image.xy(26 * 4, 21 * 4)
    }

    override fun getTarget(): Pair<Int, Int> {
        return Pair(0, 248)
    }

}